package com.demo.app.interfaces.controller.account.response;

import com.demo.app.interfaces.controller.card.response.CardVO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


/**
 * The type Account vo.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountVO {
    private Long id;
    private String email;
    private String status;
    private String contractId;
    private String info;
    private List<CardVO> cards;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
