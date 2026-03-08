package com.example.simulator.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "order-events";

    public void sendOrderEvent(Long orderId, String memberName, String stockName) {
        // 실무에서는 JSON 객체로 보내지만, 우선 간단하게 문자열로 보냅니다.
        String message = String.format("주문 완료 - 번호: %d, 고객: %s, 종목: %s", orderId, memberName, stockName);
        kafkaTemplate.send(TOPIC, message);
    }
}
