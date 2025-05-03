package com.demo.app.service.common.entity.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


/**
 * The type Account dto.
 */
@Data
public class AccountDTO {
    private Long id;
    private String email;
    private String status;
    private String contractId;
    private String info;
    private List<CardDTO> cards;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
