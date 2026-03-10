package com.example.simulator.infra;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {

    @KafkaListener(topics = "order-events", groupId = "stock-group")
    public void consume(String message) {
        // 실제 서비스라면 여기서 SMS 발송 API를 호출하거나,
        // 실시간 통계 DB를 업데이트하는 로직이 들어갑니다.
        System.out.println("==== [Kafka Consumer] 메시지 수신 완료 ====");
        System.out.println("수신된 내용: " + message);
        System.out.println("==========================================");
    }
}
