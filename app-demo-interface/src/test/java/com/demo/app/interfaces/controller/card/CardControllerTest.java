package com.demo.app.interfaces.controller.card;

import com.demo.app.domain.model.card.Card;
import com.demo.app.domain.model.card.CardStatus;
import com.demo.app.domain.service.internal.impl.RandomRFIDGenerator;
import com.demo.app.interfaces.common.convertor.WebEntityConvertor;
import com.demo.app.interfaces.controller.card.request.AssignCardRequest;
import com.demo.app.interfaces.controller.card.request.ChangeCardStatusRequest;
import com.demo.app.interfaces.controller.card.request.CreateCardRequest;
import com.demo.app.service.common.entity.request.AssignCardCommand;
import com.demo.app.service.common.entity.request.CreateCardCommand;
import com.demo.app.service.common.entity.request.UpdateCardCommand;
import com.demo.app.service.service.AccountCardBizService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The type Card controller test.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CardController Integration Tests")
class CardControllerTest {
    private static final Long CARD_ID = 100L;
    private static final Long ACCOUNT_ID = 1L;
    private static final String CARD_INFO = "test card info";

    private MockMvc mockMvc;
    @Mock
    private AccountCardBizService bizService;
    @Spy
    private WebEntityConvertor convertor = Mappers.getMapper(WebEntityConvertor.class);
    @InjectMocks
    private CardController controller;
    private Card testCard;

    /**
     * Sets .
     */
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        testCard = new Card();
        testCard.setId(CARD_ID);
        testCard.setRfid(new RandomRFIDGenerator().generate(CARD_INFO));
        testCard.setInfo(CARD_INFO);
        testCard.setStatus(CardStatus.CREATED);
        testCard.setCreateTime(LocalDateTime.now());
        testCard.setUpdateTime(LocalDateTime.now());
    }

    /**
     * The type Creation tests.
     */
    @Nested
    @DisplayName("Card Creation")
    class CreationTests {
        private CreateCardRequest validRequest() {
            return new CreateCardRequest(CARD_INFO);
        }

        /**
         * Create card success.
         *
         * @throws Exception the exception
         */
        @Test
        @DisplayName("should create card with valid request")
        void createCardSuccess() throws Exception {
            var command = CreateCardCommand.builder().info(CARD_INFO).build();

            when(bizService.createCard(command)).thenReturn(testCard);

            mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cards")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(validRequest())))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.ok").value(true),
                            jsonPath("$.data.id").value(CARD_ID),
                            jsonPath("$.data.info").value(CARD_INFO),
                            jsonPath("$.data.createTime").exists()
                    );
        }

        /**
         * Create with invalid info.
         *
         * @param invalidInfo the invalid info
         * @throws Exception the exception
         */
        @ParameterizedTest
        @MethodSource("invalidInfoProvider")
        @DisplayName("reject invalid info length")
        void createWithInvalidInfo(String invalidInfo) throws Exception {
            var request = new CreateCardRequest(invalidInfo);

            mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cards")
                            .content(new ObjectMapper().writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        /**
         * Invalid info provider stream.
         *
         * @return the stream
         */
        static Stream<Arguments> invalidInfoProvider() {
            return Stream.of(
                    Arguments.of("a".repeat(256)),
                    Arguments.of("a".repeat(1000)));
        }
    }

    /**
     * The type Status tests.
     */
    @Nested
    @DisplayName("Status Management")
    class StatusTests {
        private ChangeCardStatusRequest requestWithStatus(CardStatus status) {
            return new ChangeCardStatusRequest(status);
        }

        /**
         * Activate card success.
         *
         * @throws Exception the exception
         */
        @Test
        @DisplayName("update card status to activated")
        void activateCardSuccess() throws Exception {
            var command = UpdateCardCommand.builder()
                    .id(CARD_ID)
                    .status(CardStatus.ACTIVATED)
                    .build();

            when(bizService.updateCardStatus(command)).thenReturn(CardStatus.ACTIVATED);

            mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/cards/{id}/status", CARD_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(requestWithStatus(CardStatus.ACTIVATED))))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.data.id").value(CARD_ID),
                            jsonPath("$.data.status").value(CardStatus.ACTIVATED.toString())
                    );
        }

        /**
         * Update status without body.
         *
         * @throws Exception the exception
         */
        @Test
        @DisplayName("reject status update without body")
        void updateStatusWithoutBody() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/cards/{id}/status", CARD_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isBadRequest());
        }
    }

    /**
     * The type Assignment tests.
     */
    @Nested
    @DisplayName("Card Assignment")
    class AssignmentTests {
        private AssignCardRequest validRequest() {
            return new AssignCardRequest(ACCOUNT_ID);
        }

        /**
         * Assign card success.
         *
         * @throws Exception the exception
         */
        @Test
        @DisplayName("assign card to account successfully")
        void assignCardSuccess() throws Exception {
            var command = AssignCardCommand.builder()
                    .cardId(CARD_ID)
                    .accountId(ACCOUNT_ID)
                    .build();

            mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/cards/{id}/assign", CARD_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(validRequest())))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.data.id").value(CARD_ID),
                            jsonPath("$.data.accountId").value(ACCOUNT_ID)
                    );

            verify(bizService).assignCard(command);
        }

        /**
         * Assign with invalid account id.
         *
         * @param invalidId the invalid id
         * @throws Exception the exception
         */
        @ParameterizedTest
        @ValueSource(longs = {0L, -1L})
        @DisplayName("reject invalid account id")
        void assignWithInvalidAccountId(Long invalidId) throws Exception {
            var request = new AssignCardRequest(invalidId);

            mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/cards/{id}/assign", CARD_ID)
                            .content(new ObjectMapper().writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }

    /**
     * The type Retrieval tests.
     */
    @Nested
    @DisplayName("Card Retrieval")
    class RetrievalTests {
        /**
         * Gets card success.
         *
         * @throws Exception the exception
         */
        @Test
        @DisplayName("get existing card details")
        void getCardSuccess() throws Exception {
            when(bizService.getCardById(CARD_ID)).thenReturn(Optional.of(testCard));

            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cards/{id}", CARD_ID))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.data.id").value(CARD_ID),
                            jsonPath("$.data.info").value(CARD_INFO),
                            jsonPath("$.data.rfid.uid").value(testCard.getRfid().uid()),
                            jsonPath("$.data.rfid.visibleNumber").value(testCard.getRfid().visibleNumber()),
                            jsonPath("$.data.createTime").exists()
                    );
        }

        /**
         * Gets card not found.
         *
         * @throws Exception the exception
         */
        @Test
        @DisplayName("handle non-existent card")
        void getCardNotFound() throws Exception {
            when(bizService.getCardById(CARD_ID)).thenReturn(Optional.empty());

            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cards/{id}", CARD_ID))
                    .andExpect(jsonPath("$.data").doesNotExist());
        }
    }
}