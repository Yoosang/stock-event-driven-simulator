package com.example.simulator.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderRequest {
    private Long memberId;
    private String stockCode;
    private Integer quantity;
}
