package com.example.simulator.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private String code;    // 에러 구분 코드
    private String message; // 메시지
}
