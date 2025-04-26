package com.demo.app.service.dto.response;

import lombok.Data;

import java.time.LocalDateTime;


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
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
