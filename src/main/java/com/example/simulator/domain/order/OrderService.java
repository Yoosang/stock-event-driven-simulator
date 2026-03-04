package com.example.simulator.domain.order;

import com.example.simulator.domain.member.Member;
import com.example.simulator.domain.member.MemberRepository;
import com.example.simulator.domain.stock.Stock;
import com.example.simulator.domain.stock.StockRepository;
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

    @Transactional
    public Long order(Long memberId, String stockCode, Long price, Integer quantity) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        Stock stock = stockRepository.findById(stockCode)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 종목입니다."));

        member.decreaseBalance(price * quantity);

        Order order = Order.createOrder(member, stock, price, quantity);
        orderRepository.save(order);

        return order.getId();
    }
}
