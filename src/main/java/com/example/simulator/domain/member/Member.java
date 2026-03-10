package com.example.simulator.domain.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Long balance;

    // 잔액 차감
    public void decreaseBalance(Long amount) {
        if (this.balance < amount) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }
        this.balance -= amount;
    }

    // 정적 팩토리 메서드: 생성자 대신 사용
    public static Member createMember(String name, Long initialBalance) {
        Member member = new Member();
        member.name = name;
        member.balance = initialBalance;
        return member;
    }

}
