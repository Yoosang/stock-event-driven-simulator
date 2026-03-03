package com.example.simulator.domain.stock;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock {

    @Id
    @Column(name = "stock_code")
    private String stockCode;

    @Column(name ="stock_name", nullable = false, length = 20)
    private String stockName;

    public static Stock createStock(String stockCode, String stockName) {
        Stock stock = new Stock();
        stock.stockCode = stockCode;
        stock.stockName = stockName;
        return stock;
    }
}
