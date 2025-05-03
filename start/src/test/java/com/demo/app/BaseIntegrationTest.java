package com.demo.app;

import com.demo.app.config.AppTestConfig;
import com.demo.app.domain.model.account.AccountStatus;
import com.demo.app.domain.model.card.CardStatus;
import com.demo.app.infrastructure.repository.account.mapper.AccountMapper;
import com.demo.app.infrastructure.repository.card.CardDO;
import com.demo.app.infrastructure.repository.card.mapper.CardMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The type Account card integration test.
 */
@DisplayName("Base Integration Test")
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/cleanup.sql", executionPhase = AFTER_TEST_METHOD)
@Import(AppTestConfig.class)
class BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private CardMapper cardMapper;
    private static final String VALID_EMAIL = "test@test.com";
    private static final String ACCOUNT_INFO = "test account info";
    private static final String CARD_INFO = "test card info";

    /**
     * Full flow test.
     *
     * @throws Exception the exception
     */
    @Test
    @DisplayName("Typical Flow Test for Account&Card")
    void fullFlowTest() throws Exception {
        // 1. Create an account
        var accountId = createAccount();
        // 2. Create a card
        var cardId = createCard();
        // 3. Assign this card
        assignCard(accountId, cardId);
        // 4. Query card info
        verifyAssociation(accountId, cardId);
        // 5. Query pagination
        verifyPagination(accountId);
    }

    private Long createAccount() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email": "%s",
                                    "info":  "%s"
                                }
                                """.formatted(VALID_EMAIL, ACCOUNT_INFO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").isNumber())
                .andExpect(jsonPath("$.data.contractId").exists())
                .andExpect(jsonPath("$.data.email").value(VALID_EMAIL))
                .andExpect(jsonPath("$.data.info").value(ACCOUNT_INFO))
                .andExpect(jsonPath("$.data.status").value(AccountStatus.CREATED.toString()))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        return JsonPath.parse(response).read("$.data.id", Long.class);
    }

    private Long createCard() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "info": "%s"
                                }
                                """.formatted(CARD_INFO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").isNumber())
                .andExpect(jsonPath("$.data.rfid").exists())
                .andExpect(jsonPath("$.data.info").value(CARD_INFO))
                .andExpect(jsonPath("$.data.status").value(CardStatus.CREATED.toString()))
                .andReturn();

        return JsonPath.parse(result.getResponse().getContentAsString())
                .read("$.data.id", Long.class);
    }

    private void assignCard(Long accountId, Long cardId) throws Exception {
        mockMvc.perform(patch("/api/v1/cards/{id}/assign", cardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                {
                                    "accountId": %d
                                }
                                """, accountId)
                        )).andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(cardId))
                .andExpect(jsonPath("$.data.accountId").value(accountId))
                .andReturn();
    }

    private void verifyAssociation(Long accountId, Long cardId) {
        CardDO card = cardMapper.selectById(cardId);
        assertThat(card)
                .isNotNull()
                .satisfies(c -> {
                    assertThat(c.getAccountId()).isEqualTo(accountId);
                    assertThat(c.getStatus()).isEqualTo(CardStatus.ASSIGNED);
                });
    }

    private void verifyPagination(Long accountId) throws Exception {
        mockMvc.perform(get("/api/v1/accounts")
                        .param("page", "1")
                        .param("size", "10")
                        .param("withCards", "true"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.total").value(1),
                        jsonPath("$.data.totalPage").value(1),
                        jsonPath("$.data.list[0].id").value(accountId),
                        jsonPath("$.data.list[0].cards").isArray(),
                        jsonPath("$.data.list[0].cards[0].status").value(CardStatus.ASSIGNED.toString())
                );
    }
}