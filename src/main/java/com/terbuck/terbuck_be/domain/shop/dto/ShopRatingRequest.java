package com.terbuck.terbuck_be.domain.shop.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShopRatingRequest {

    @NotNull(message = "score 값은 필수입니다.")
    @Min(value = 1, message = "score 값은 1 이상이어야 합니다.")
    @Max(value = 5, message = "score 값은 5 이하여야 합니다.")
    private Integer score;
}
