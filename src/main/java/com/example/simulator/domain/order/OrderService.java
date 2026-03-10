package com.example.simulator.domain.order;

import com.example.simulator.domain.member.Member;
import com.example.simulator.domain.member.MemberRepository;
import com.example.simulator.domain.stock.PriceService;
import com.example.simulator.domain.stock.Stock;
import com.example.simulator.domain.stock.StockRepository;
import com.example.simulator.infra.OrderEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final MemberRepository memberRepository;
    private final StockRepository stockRepository;
    private final OrderRepository orderRepository;
    private final PriceService priceService;
    private final OrderEventProducer orderEventProducer;

    // 현재가 주문
    @Transactional
    public Long order(Long memberId, String stockCode, Integer quantity) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        Stock stock = stockRepository.findById(stockCode)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 종목입니다."));

        Long currentPrice = priceService.getLatestPrice(stockCode);
        if (currentPrice <= 0) {
            throw new IllegalStateException("현재 시세 정보가 없어 주문이 불가능합니다.");
        }

        member.decreaseBalance(currentPrice * quantity);

        Order order = Order.createOrder(member, stock, currentPrice, quantity);
        orderRepository.save(order);
        orderEventProducer.sendOrderEvent(order.getId(), member.getName(), stock.getStockName());

        return order.getId();
    }
}
