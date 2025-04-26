package com.demo.app.service.dto.response;

import com.demo.app.domain.model.card.CardStatus;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * The type Account dto.
 */
@Data
public class CardDTO {
    private Long id;
    private Long accountId;
    private CardStatus status;
    private String info;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
