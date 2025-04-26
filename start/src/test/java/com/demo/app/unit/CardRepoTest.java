package com.demo.app.unit;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.app.domain.common.entity.CommonPage;
import com.demo.app.domain.common.entity.CommonPageInfo;
import com.demo.app.domain.model.card.Card;
import com.demo.app.domain.model.card.CardStatus;
import com.demo.app.infrastructure.common.convertor.EntityConvertor;
import com.demo.app.infrastructure.common.exception.InfrastructureException;
import com.demo.app.infrastructure.repository.card.CardDO;
import com.demo.app.infrastructure.repository.card.CardRepo;
import com.demo.app.infrastructure.repository.card.mapper.CardMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static com.demo.app.infrastructure.common.exception.InfrastructureErrorCode.OPTIMISTIC_LOCKER_FAILED;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * The type Card repo test.
 */
@ExtendWith(MockitoExtension.class)
class CardRepoTest {
    @Mock
    private CardMapper cardMapper;
    @Mock
    private EntityConvertor entityConvertor;
    @InjectMocks
    private CardRepo cardRepo;
    private Card card;
    private CardDO cardDO;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        card = new Card("Test Info");
        card.setId(1L);
        card.setAccountId(100L);
        card.setStatus(CardStatus.CREATED);
        card.setVersion(1);
        card.setCreateTime(LocalDateTime.now());
        card.setUpdateTime(LocalDateTime.now());

        cardDO = new CardDO();
        cardDO.setId(1L);
        cardDO.setAccountId(100L);
        cardDO.setStatus(CardStatus.CREATED);
        cardDO.setInfo("Test Info");
        cardDO.setVersion(1);
        cardDO.setCreateTime(card.getCreateTime());
        cardDO.setUpdateTime(card.getUpdateTime());
    }

    /**
     * Test find by account id returns populated page.
     */
    @Test
    void testFindByAccountId_ReturnsPopulatedPage() {
        // Arrange
        CommonPageInfo pageInfo = new CommonPageInfo(1L, 10L);
        Page<CardDO> page = new Page<>();
        page.setRecords(Collections.singletonList(cardDO));
        page.setTotal(1);

        when(cardMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);
        when(entityConvertor.toBO(cardDO)).thenReturn(card);

        // Act
        CommonPage<Card> result = cardRepo.findByAccountId(100L, pageInfo);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getList().size());
        assertEquals(card, result.getList().getFirst());
        assertEquals(1, result.getInfo().getPageNum());
        assertEquals(10, result.getInfo().getPageSize());
        assertEquals(1, result.getTotal());
    }

    /**
     * Test find by account id returns empty page.
     */
    @Test
    void testFindByAccountId_ReturnsEmptyPage() {
        // Arrange
        CommonPageInfo pageInfo = new CommonPageInfo(1L, 10L);
        Page<CardDO> emptyPage = new Page<>();
        emptyPage.setRecords(Collections.emptyList());
        emptyPage.setTotal(0);

        when(cardMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(emptyPage);

        // Act
        CommonPage<Card> result = cardRepo.findByAccountId(200L, pageInfo);

        // Assert
        assertNotNull(result);
        assertTrue(result.getList().isEmpty());
        assertEquals(0, result.getTotal());
    }

    /**
     * Test save successful insert.
     */
    @Test
    void testSave_SuccessfulInsert() {
        // Arrange
        when(entityConvertor.toDO(card)).thenReturn(cardDO);
        when(cardMapper.insertOrUpdate(cardDO)).thenReturn(true);
        when(entityConvertor.toBO(cardDO)).thenReturn(card);

        // Act
        Card savedCard = cardRepo.save(card);

        // Assert
        assertNotNull(savedCard);
        assertEquals(card, savedCard);
        verify(cardMapper, times(1)).insertOrUpdate(cardDO);
        verify(entityConvertor, times(1)).toDO(card);
        verify(entityConvertor, times(1)).toBO(cardDO);
    }

    /**
     * Test save throws exception on optimistic lock failure.
     */
    @Test
    void testSave_ThrowsExceptionOnOptimisticLockFailure() {
        // Arrange
        when(entityConvertor.toDO(card)).thenReturn(cardDO);
        when(cardMapper.insertOrUpdate(cardDO)).thenReturn(false);

        // Act & Assert
        assertThrows(InfrastructureException.class, () -> cardRepo.save(card));
        verify(cardMapper, times(1)).insertOrUpdate(cardDO);
    }


    /**
     * Save should throw optimistic lock exception.
     */
    @Test
    void save_shouldThrowOptimisticLockException() {
        // Arrange
        Card card = new Card("Test Card");
        card.setId(789L);
        card.setVersion(2);

        CardDO cardDO = new CardDO();
        cardDO.setId(789L);
        cardDO.setVersion(2);

        when(entityConvertor.toDO(card)).thenReturn(cardDO);
        when(cardMapper.insertOrUpdate(cardDO)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> cardRepo.save(card))
                .isInstanceOf(InfrastructureException.class)
                .hasFieldOrPropertyWithValue("errorCode", OPTIMISTIC_LOCKER_FAILED);

    }


    /**
     * Test find by id returns card when exists.
     */
    @Test
    void testFindById_ReturnsCardWhenExists() {
        // Arrange
        when(cardMapper.selectById(1L)).thenReturn(cardDO);
        when(entityConvertor.toBO(cardDO)).thenReturn(card);

        // Act
        Optional<Card> result = cardRepo.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(card, result.get());
        verify(cardMapper, times(1)).selectById(1L);
    }

    /**
     * Test find by id returns empty when not exists.
     */
    @Test
    void testFindById_ReturnsEmptyWhenNotExists() {
        // Arrange
        when(cardMapper.selectById(999L)).thenReturn(null);

        // Act
        Optional<Card> result = cardRepo.findById(999L);

        // Assert
        assertTrue(result.isEmpty());
        verify(cardMapper, times(1)).selectById(999L);
    }

    /**
     * Test exists card returns true when exists.
     */
    @Test
    void testExistsCard_ReturnsTrueWhenExists() {
        // Arrange
        when(cardMapper.exists(any(LambdaQueryWrapper.class))).thenReturn(true);

        // Act
        boolean exists = cardRepo.existsCard(1L);

        // Assert
        assertTrue(exists);
        verify(cardMapper, times(1)).exists(any(LambdaQueryWrapper.class));
    }

    /**
     * Test exists card returns false when not exists.
     */
    @Test
    void testExistsCard_ReturnsFalseWhenNotExists() {
        // Arrange
        when(cardMapper.exists(any(LambdaQueryWrapper.class))).thenReturn(false);

        // Act
        boolean exists = cardRepo.existsCard(999L);

        // Assert
        assertFalse(exists);
        verify(cardMapper, times(1)).exists(any(LambdaQueryWrapper.class));
    }
}