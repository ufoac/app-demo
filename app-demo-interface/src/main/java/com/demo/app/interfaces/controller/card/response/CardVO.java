package com.demo.app.interfaces.controller.card.response;

import com.demo.app.domain.model.card.CardStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * The type Account dto.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardVO {
    private Long id;
    private String contractId;
    private String accountId;
    private CardStatus status;
    private String info;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
