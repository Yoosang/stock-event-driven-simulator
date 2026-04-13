package com.example.simulator.infra;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AccountConsumer {
    @KafkaListener(topics = "order-events", groupId = "account-group")
    public void updateAccount(String message) {
        // 실제로는 DB의 잔고를 차감하는 핵심 로직이 들어갑니다.
        System.out.println("[Account Service] 자산 업데이트 완료: " + message);
    }
}
