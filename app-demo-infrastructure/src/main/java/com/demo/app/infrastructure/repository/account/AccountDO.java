package com.demo.app.infrastructure.repository.account;


import com.baomidou.mybatisplus.annotation.*;
import com.demo.app.domain.model.account.AccountStatus;
import com.demo.app.infrastructure.repository.card.CardDO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The type Account do.
 */
@Data
@NoArgsConstructor
@TableName(value = "account", autoResultMap = true)
public class AccountDO {
    /**
     * Primary key.
     * Set IdType.ASSIGN_ID to use Snowflake
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    private String email;
    private String contractId;
    private AccountStatus status;
    private String info;
    /**
     * Logical association (not persisted in database)
     * Represents the collection of cards associated with this account
     */
    @TableField(exist = false)
    private List<CardDO> cards;
    /**
     * Creation timestamp (autofilled on INSERT)
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * Last modification timestamp (autofilled on INSERT/UPDATE)
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