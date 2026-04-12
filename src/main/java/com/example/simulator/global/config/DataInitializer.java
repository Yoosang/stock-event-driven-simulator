package com.example.simulator.global.config;

import com.example.simulator.domain.member.Member;
import com.example.simulator.domain.member.MemberRepository;
import com.example.simulator.domain.stock.PriceService;
import com.example.simulator.domain.stock.Stock;
import com.example.simulator.domain.stock.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {
    private final MemberRepository memberRepository;
    private final StockRepository stockRepository;
    private final PriceService priceService;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // 1. 회원 데이터 초기화
            if (memberRepository.count() == 0) {
                memberRepository.save(Member.createMember("테스트1", 1000000L));
            }

            // 2. 종목 데이터 및 Redis 시세 초기화
            if (stockRepository.count() == 0) {
                stockRepository.save(Stock.createStock("005930", "삼성전자"));
                stockRepository.save(Stock.createStock("005380", "현대차"));

                // Redis에도 즉시 시세 주입
                priceService.updatePrice("005930", 75000L);
                priceService.updatePrice("005380", 210000L);
            }

            log.info("==== 로컬 개발용 기초 데이터 초기화 완료 ====");
        };
    }
}
