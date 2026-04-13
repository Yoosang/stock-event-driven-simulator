package com.example.simulator.infra;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {
    @KafkaListener(topics = "order-events", groupId = "notification-group")
    public void sendNotification(String message) throws InterruptedException {
        // 일부러 3초간 지연을 발생시켜 비동기의 위력을 확인합니다.
        Thread.sleep(3000);
        System.out.println("[Notification Service] 카톡 알림 발송 완료 (3초 지연됨)");
    }
}
