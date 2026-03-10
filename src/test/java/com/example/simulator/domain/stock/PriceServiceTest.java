package com.example.simulator.domain.stock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PriceServiceTest {
    @Autowired
    private PriceService priceService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    @DisplayName("Redis에 종목 시세를 저장하고 다시 조회할 수 있어야 한다")
    void redis_price_save_and_get() {
        // Given
        String stockCode = "stock001";
        Long expectedPrice = 50500L;

        // When
        priceService.updatePrice(stockCode, expectedPrice);
        Long actualPrice = priceService.getLatestPrice(stockCode);

        // Then
        assertEquals(expectedPrice, actualPrice, "저장한 시세와 조회한 시세가 일치");
    }

    @Test
    @DisplayName("시세 정보가 없는 종목을 조회하면 0을 반환해야 한다")
    void redis_price_get_empty() {
        // Given
        String unknownCode = "999999";

        // When
        Long actualPrice = priceService.getLatestPrice(unknownCode);

        // Then
        assertEquals(0L, actualPrice, "데이터가 없을 경우 0");
    }

}