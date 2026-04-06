package com.example.simulator.infra;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {

    @KafkaListener(topics = "order-events", groupId = "stock-group")
    @RetryableTopic(    //자동으로 dlt topic에 저장해 주는 기능
            attempts = "5", //재시도 횟수
            backoff = @Backoff(delay = 1000, multiplier = 2) ,//재시도 1초 간격으로, 시간을 2배수로 늘려가며 ex. 1초 2초 4초 ...
            dltTopicSuffix = ".dlt" // dlt topic
    )
    public void consume(String message) {
        // 실제 서비스라면 여기서 SMS 발송 API를 호출하거나,
        // 실시간 통계 DB를 업데이트하는 로직이 들어갑니다.
        System.out.println("==== [Kafka Consumer] 메시지 수신 완료 ====");
        System.out.println("수신된 내용: " + message);
        System.out.println("==========================================");
    }
}
