package com.example.simulator.domain.stock;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RankingService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String RANKING_KEY = "stock:ranking:volume";

    // 거래 발생 시 거래량 누적 업데이트
    public void updateVolume(String stockName, Integer quantity) {
        redisTemplate.opsForZSet().incrementScore(RANKING_KEY, stockName, quantity);
    }

    // 상위 5개 종목 조회
    public Set<ZSetOperations.TypedTuple<Object>> getTopRanking() {
        // 점수(거래량)가 높은 순으로 0위부터 4위까지 조회
        return redisTemplate.opsForZSet().reverseRangeWithScores(RANKING_KEY, 0, 4);
    }
}
