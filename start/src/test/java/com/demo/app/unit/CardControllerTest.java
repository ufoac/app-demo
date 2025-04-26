package com.demo.app.unit;

import com.demo.app.domain.model.card.Card;
import com.demo.app.interfaces.common.convertor.WebEntityConvertor;
import com.demo.app.interfaces.controller.card.CardController;
import com.demo.app.interfaces.controller.card.request.AssignCardRequest;
import com.demo.app.interfaces.controller.card.request.ChangeCardStatusRequest;
import com.demo.app.interfaces.controller.card.request.CreateCardRequest;
import com.demo.app.interfaces.controller.card.response.CardVO;
import com.demo.app.service.service.AccountCardBizService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static com.demo.app.domain.model.card.CardStatus.ASSIGNED;
import static com.demo.app.domain.model.card.CardStatus.CREATED;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The type Card controller test.
 */
@WebMvcTest(CardController.class)
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountCardBizService accountCardBizService;

    @MockBean
    private WebEntityConvertor webEntityConvertor;

    /**
     * Create card should return created card.
     *
     * @throws Exception the exception
     */
    @Test
    void createCard_shouldReturnCreatedCard() throws Exception {
        // Prepare test data
        CreateCardRequest request = new CreateCardRequest();
        request.setInfo("Test Card");

        Card mockCard = new Card("Test Card");
        mockCard.setId(1L);
        mockCard.setCreateTime(LocalDateTime.now());

        Mockito.when(accountCardBizService.createCard(any()))
                .thenReturn(mockCard);
        Mockito.when(webEntityConvertor.toVO(mockCard))
                .thenReturn(buildCardVO(mockCard));

        // Execute request and verify
        mockMvc.perform(post("/api/v1/card/card/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.status").value(CREATED.toString()));
    }

    /**
     * Update card status should return updated card.
     *
     * @throws Exception the exception
     */
    @Test
    void updateCardStatus_shouldReturnUpdatedCard() throws Exception {
        // Prepare test data
        ChangeCardStatusRequest request = new ChangeCardStatusRequest();
        request.setId(1L);
        request.setStatus(ASSIGNED);

        Card updatedCard = new Card("Updated");
        updatedCard.setId(1L);
        updatedCard.setStatus(ASSIGNED);

        Mockito.when(accountCardBizService.updateCardStatus(any()))
                .thenReturn(updatedCard);
        Mockito.when(webEntityConvertor.toVO(updatedCard))
                .thenReturn(buildCardVO(updatedCard));

        // Execute request and verify
        mockMvc.perform(post("/api/v1/card/card/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value(ASSIGNED.toString()));
    }

    /**
     * Gets card detail should return not found.
     *
     * @throws Exception the exception
     */
    @Test
    void getCardDetail_shouldReturnNotFound() throws Exception {
        // Mock empty result
        Mockito.when(accountCardBizService.getCardById(999L))
                .thenReturn(null);

        // Execute request and verify
        mockMvc.perform(get("/api/v1/card/detail/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    /**
     * Assign card should return updated card.
     *
     * @throws Exception the exception
     */
    @Test
    void assignCard_shouldReturnUpdatedCard() throws Exception {
        // Prepare test data
        AssignCardRequest request = new AssignCardRequest();
        request.setAccountId(1001L);
        request.setCardId(2002L);

        Card assignedCard = new Card("Assigned Card");
        assignedCard.setId(2002L);
        assignedCard.setAccountId(1001L);
        assignedCard.setStatus(ASSIGNED);

        Mockito.when(accountCardBizService.assignCard(any()))
                .thenReturn(assignedCard);
        Mockito.when(webEntityConvertor.toVO(assignedCard))
                .thenReturn(buildCardVO(assignedCard));

        // Execute request and verify
        mockMvc.perform(post("/api/v1/card/card/assign")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accountId").value("1001"))
                .andExpect(jsonPath("$.data.status").value(ASSIGNED.toString()));
    }

    private CardVO buildCardVO(Card card) {
        CardVO vo = new CardVO();
        vo.setId(card.getId());
        vo.setStatus(card.getStatus());
        vo.setInfo(card.getInfo());
        vo.setCreateTime(card.getCreateTime());
        vo.setAccountId(card.getAccountId() != null ?
                card.getAccountId().toString() :
                null);
        return vo;
    }
}