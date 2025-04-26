package com.demo.app.infrastructure.repository.card;


import com.baomidou.mybatisplus.annotation.*;
import com.demo.app.domain.model.card.CardStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * The type Card.
 */
@Data
@NoArgsConstructor
@TableName(value = "card", autoResultMap = true)
public class CardDO {
    /**
     * Primary key.
     * Change IdType.ASSIGN_ID to use Snowflake
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long accountId;
    private CardStatus status;
    private String info;
    /**
     * Creation timestamp (auto-filled on INSERT)
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * Last modification timestamp (auto-filled on INSERT/UPDATE)
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * Optimistic lock version field
     * Ensures concurrent modification safety
     */
    @Version
    private Integer version;
}