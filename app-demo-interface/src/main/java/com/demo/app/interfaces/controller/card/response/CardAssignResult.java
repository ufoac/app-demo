package com.demo.app.interfaces.controller.card.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;


/**
 * The type Account dto.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardAssignResult {
    private Long id;
    private Long accountId;
}
