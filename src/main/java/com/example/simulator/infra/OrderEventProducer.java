package com.example.simulator.infra;

import com.example.simulator.infra.dto.OrderEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private static final String TOPIC = "order-events";

    public void sendOrderEvent(OrderEvent orderEvent) {
        try {
            String message = objectMapper.writeValueAsString(orderEvent);
            kafkaTemplate.send(TOPIC, message);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("Kafka 메시지 변환 실패", e);
        }
    }
}
