package com.example.simulator.domain.order;

import com.example.simulator.domain.member.Member;
import com.example.simulator.domain.member.MemberRepository;
import com.example.simulator.domain.stock.Stock;
import com.example.simulator.domain.stock.StockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired
    OrderService orderService;
    @Autowired MemberRepository memberRepository;
    @Autowired StockRepository stockRepository;
    @Autowired OrderRepository orderRepository;

    @Test
    @DisplayName("주문 완료")
    void order_success() {
        //given
        Member member = Member.createMember("Yoosang", 1000000L);
        memberRepository.save(member);
        Stock stock = Stock.createStock("stock001", "테스트종목");
        stockRepository.save(stock);

        Long price = 10000L;
        int quantity = 5;

        //when
        Long orderId = orderService.order(member.getId(), stock.getStockCode(), price, quantity);

        //then
        Order getOrder = orderRepository.findById(orderId).orElseThrow();

        // 주문 상태 검증
        assertEquals(price, getOrder.getOrderPrice(), "주문 가격이 일치해야 한다");
        assertEquals(quantity, getOrder.getQuantity(), "주문 수량이 일치해야 한다");

        // 잔액 차감 검증 (10만 원 - 7만 원 = 3만 원)
        Member updatedMember = memberRepository.findById(member.getId()).orElseThrow();
        assertEquals(950000L, updatedMember.getBalance(), "잔액이 정상적으로 차감되어야 한다");

    }

    @Test
    @DisplayName("잔액이 부족, 주문 실패")
    void order_fail_balance_shortage() {
        // Given
        Member member = Member.createMember("Yoosang", 10000L); // 1만원만 보유
        memberRepository.save(member);

        Stock stock = Stock.createStock("stock002", "테스트종목2");
        stockRepository.save(stock);

        // When & Then (실행과 동시에 에러가 발생하는지 검증)
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.order(member.getId(), stock.getStockCode(), 50000L, 1);
        }, "잔액 부족 시 예외가 발생해야 한다");
    }
}