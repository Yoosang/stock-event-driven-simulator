package com.example.simulator.domain.order;

import com.example.simulator.domain.member.Member;
import com.example.simulator.domain.member.MemberRepository;
import com.example.simulator.domain.stock.PriceService;
import com.example.simulator.domain.stock.RankingService;
import com.example.simulator.domain.stock.Stock;
import com.example.simulator.domain.stock.StockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired
    OrderService orderService;
    @Autowired MemberRepository memberRepository;
    @Autowired StockRepository stockRepository;
    @Autowired OrderRepository orderRepository;
    @Autowired PriceService priceService;
    @Autowired RankingService rankingService;

    @Test
    @DisplayName("주문 완료")
    void order_success() {
        //given
        Member member = Member.createMember("Yoosang", 1000000L);
        memberRepository.save(member);
        Stock stock = Stock.createStock("stock001", "테스트종목");
        stockRepository.save(stock);

        int quantity = 5;
        priceService.updatePrice("stock001", 50500L);

        //when
        Long orderId = orderService.order(member.getId(), stock.getStockCode(), quantity);

        //then
        Order getOrder = orderRepository.findById(orderId).orElseThrow();

        // 주문 상태 검증
        assertEquals(priceService.getLatestPrice("stock001"), getOrder.getOrderPrice(), "주문 가격이 일치해야 한다");
        assertEquals(quantity, getOrder.getQuantity(), "주문 수량이 일치해야 한다");

        // 잔액 차감 검증 (10만 원 - 7만 원 = 3만 원)
        Member updatedMember = memberRepository.findById(member.getId()).orElseThrow();
        assertEquals(747500L, updatedMember.getBalance(), "잔액이 정상적으로 차감되어야 한다");

    }

    @Test
    @DisplayName("잔액이 부족, 주문 실패")
    void order_fail_balance_shortage() {
        // Given
        Member member = Member.createMember("Yoosang", 10000L); // 1만원만 보유
        memberRepository.save(member);

        Stock stock = Stock.createStock("stock002", "테스트종목2");
        stockRepository.save(stock);

        priceService.updatePrice("stock002", 50500L);

        // When & Then (실행과 동시에 에러가 발생하는지 검증)
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.order(member.getId(), stock.getStockCode(), 1);
        }, "잔액 부족 시 예외가 발생해야 한다");
    }

    @Test
    @DisplayName("실시간 거래량 랭킹 테스트: 여러 종목 주문 시 Redis 정렬 확인")
    void ranking_test() throws InterruptedException {
        // 1. Given: 종목 준비 및 시세 주입
        Stock samsung = Stock.createStock("005930", "삼성전자");
        Stock hyundai = Stock.createStock("005380", "현대차");
        stockRepository.save(samsung);
        stockRepository.save(hyundai);

        priceService.updatePrice(samsung.getStockCode(), 70000L);
        priceService.updatePrice(hyundai.getStockCode(), 200000L);

        Member member = Member.createMember("테스터", 5000000L);
        memberRepository.save(member);

        // 2. When: 서로 다른 수량으로 주문 실행
        // 삼성전자 10주 (총 10), 현대차 20주 (총 20) -> 현대차가 1위여야 함
        orderService.order(member.getId(), samsung.getStockCode(), 10);
        orderService.order(member.getId(), hyundai.getStockCode(), 20);

        // Kafka Consumer가 Redis 랭킹을 업데이트할 시간을 잠시 줍니다.
        Thread.sleep(1500);

        // 3. Then: Redis 랭킹 검증
        Set<ZSetOperations.TypedTuple<Object>> topRanking = rankingService.getTopRanking();

        // 랭킹 리스트의 첫 번째 요소가 '현대차'인지 확인
        ZSetOperations.TypedTuple<Object> next = topRanking.iterator().next();

        assertEquals("현대차", next.getValue(), "거래량이 더 많은 현대차가 1위여야 합니다.");

        System.out.println("현재 1위 종목: " + next.getValue() + " (거래량: " + next.getScore() + ")");
    }
}