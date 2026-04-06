package com.example.simulator.infra.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {
    private Long orderId;
    private Long memberId;
    private String memberName;
    private String stockCode;
    private String stockName;
    private Long orderPrice;
    private Integer quantity;
}
