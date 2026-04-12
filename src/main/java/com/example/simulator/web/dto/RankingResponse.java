package com.example.simulator.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RankingResponse {
    private String stockName;
    private Double volume;
}
