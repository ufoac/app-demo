package com.demo.app.service.common.entity.response;

import com.demo.app.domain.model.card.CardStatus;
import com.demo.app.domain.model.card.RFID;
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
    private RFID rfid;
    private String info;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
