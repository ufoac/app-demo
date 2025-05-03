package com.demo.app.infrastructure.repository.card;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.app.domain.common.entity.CommonPageInfo;
import com.demo.app.domain.model.card.Card;
import com.demo.app.domain.model.card.CardStatus;
import com.demo.app.domain.model.card.RFID;
import com.demo.app.infrastructure.common.convertor.EntityConvertor;
import com.demo.app.infrastructure.common.exception.InfrastructureException;
import com.demo.app.infrastructure.repository.card.mapper.CardMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.demo.app.infrastructure.common.exception.InfrastructureErrorCode.OPTIMISTIC_LOCKER_FAILED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The type Card repo test.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Card Repository Tests")
class CardRepoTest {
    @Mock
    private CardMapper cardMapper;
    @InjectMocks
    private CardRepo cardRepo;
    @Spy
    private EntityConvertor entityConvertor = Mappers.getMapper(EntityConvertor.class);
    private Card testCard;
    private CardDO testCardDO;

    private static class TestData {
        /**
         * Create card.
         *
         * @return the card
         */
        static Card createCard() {
            Card card = new Card();
            card.setId(1L);
            card.setAccountId(1001L);
            card.setStatus(CardStatus.ACTIVATED);
            card.setRfid(new RFID("A1B2C3D4E5F6G7H8", "ABCDE12345"));
            card.setVersion(1);
            return card;
        }

        /**
         * Create card do.
         *
         * @return the card do
         */
        static CardDO createCardDO() {
            CardDO cardDO = new CardDO();
            cardDO.setId(1L);
            cardDO.setAccountId(1001L);
            cardDO.setRfid(new RFID("A1B2C3D4E5F6G7H8", "ABCDE12345"));
            cardDO.setStatus(CardStatus.ACTIVATED);
            cardDO.setVersion(1);
            return cardDO;
        }
    }

    /**
     * Sets .
     */
    @BeforeEach
    void setup() {
        testCard = TestData.createCard();
        testCardDO = TestData.createCardDO();
    }

    /**
     * Save success.
     */
    @Test
    @DisplayName("Save card successfully")
    void save_Success() {
        when(cardMapper.insertOrUpdate((CardDO) any())).thenReturn(true);

        Card result = cardRepo.save(testCard);

        assertThat(result.getId()).isEqualTo(1L);
        verify(cardMapper).insertOrUpdate(argThat((CardDO data) ->
                data.getAccountId() == 1001L
        ));
    }

    /**
     * Save optimistic lock failure.
     */
    @Test
    @DisplayName("Throw on optimistic lock failure")
    void save_OptimisticLockFailure() {
        when(cardMapper.insertOrUpdate((CardDO) any())).thenReturn(false);

        assertThatThrownBy(() -> cardRepo.save(testCard))
                .isInstanceOf(InfrastructureException.class)
                .hasFieldOrPropertyWithValue("errorCode", OPTIMISTIC_LOCKER_FAILED);
    }

    /**
     * Update status.
     */
    @Test
    @DisplayName("Update card status")
    void updateStatus() {
        when(cardMapper.updateById(argThat((CardDO data) ->
                data.getStatus() == CardStatus.ACTIVATED
        ))).thenReturn(1);

        cardRepo.changeStatus(testCard);

        verify(cardMapper).updateById(argThat((CardDO data) ->
                data.getVersion() == 1 &&
                        data.getId() == 1L
        ));
    }

    /**
     * Find by id exists.
     */
    @Test
    @DisplayName("Find card by existing ID")
    void findById_Exists() {
        when(cardMapper.selectById(1L)).thenReturn(testCardDO);

        Optional<Card> result = cardRepo.findById(1L);
        assertThat(result).get().returns(1001L, Card::getAccountId);
    }


    /**
     * Find by account id should return paged results.
     */
    @Test
    @DisplayName("Query cards by account with pagination")
    void findByAccountId() {
        Page<CardDO> mockPage = new Page<>(1, 10);
        mockPage.setRecords(List.of(testCardDO));

        var queryCaptor = ArgumentCaptor.forClass(LambdaQueryWrapper.class);
        when(cardMapper.<Page<CardDO>>selectPage(any(Page.class), queryCaptor.capture()))
                .thenReturn(mockPage);
        Page<CardDO> result = cardRepo.findByAccountId(
                1001L,
                new CommonPageInfo((long) 1, (long) 10)
        );

        assertThat(result.getRecords())
                .hasSize(1)
                .first()
                .returns(1001L, CardDO::getAccountId);
    }
}