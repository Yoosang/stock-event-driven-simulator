package com.example.simulator.domain.stock;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceService {
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String PRICE_KEY_PREFIX = "STOCK_PRICE:";

    // 시세 업데이트 (Key: STOCK_PRICE:005930, Value: 80000)
    public void updatePrice(String stockCode, Long price) {
        redisTemplate.opsForValue().set(PRICE_KEY_PREFIX + stockCode, price.toString());
    }

    // 시세 조회
    public Long getLatestPrice(String stockCode) {
        Object price = redisTemplate.opsForValue().get(PRICE_KEY_PREFIX + stockCode);
        return price != null ? Long.parseLong(price.toString()) : 0L;
    }

}
