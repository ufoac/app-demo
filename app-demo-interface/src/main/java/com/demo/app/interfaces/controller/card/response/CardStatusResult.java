package com.demo.app.interfaces.controller.card.response;

import com.demo.app.domain.model.card.CardStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;


/**
 * The type Account dto.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardStatusResult {
    private Long id;
    private CardStatus status;
}
