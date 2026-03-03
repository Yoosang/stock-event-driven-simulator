package com.example.simulator.domain.order;

import com.example.simulator.domain.member.Member;
import com.example.simulator.domain.stock.Stock;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    //주문번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_code")
    private Stock stock;

    private Long orderPrice;
    private Integer quantity;
    private LocalDateTime orderTime;

    // 생성 메서드
    public static Order createOrder(Member member, Stock stock, Long price, Integer quantity) {
        Order order = new Order();
        order.member = member;
        order.stock = stock;
        order.orderPrice = price;
        order.quantity = quantity;
        order.orderTime = LocalDateTime.now();
        return order;
    }
}
