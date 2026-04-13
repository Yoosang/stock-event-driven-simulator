package com.example.simulator.infra;

import com.example.simulator.domain.stock.RankingService;
import com.example.simulator.infra.dto.OrderEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RankingConsumer {
    private final RankingService rankingService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "order-events", groupId = "ranking-group")
    public void updateRanking(String message) throws JsonProcessingException {
        OrderEvent event = objectMapper.readValue(message, OrderEvent.class);

        // 실시간 거래량 랭킹 업데이트
        rankingService.updateVolume(event.getStockName(), event.getQuantity());

        System.out.println("[Ranking Service] 랭킹 업데이트 완료: " + event.getStockName());
    }
}
