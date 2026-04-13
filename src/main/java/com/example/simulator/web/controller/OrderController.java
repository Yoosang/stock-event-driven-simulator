package com.example.simulator.web.controller;

import com.example.simulator.domain.order.OrderService;
import com.example.simulator.domain.stock.RankingService;
import com.example.simulator.web.dto.OrderRequest;
import com.example.simulator.web.dto.RankingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/order/v1")
public class OrderController {

    private final OrderService orderService;
    private final RankingService rankingService;

    @PostMapping("/orders")
    public ResponseEntity<Long> createOrder(@RequestBody OrderRequest orderRequest) {
        log.info("Create order request: {}", orderRequest);
        Long orderId = orderService.order(
                orderRequest.getMemberId(),
                orderRequest.getStockCode(),
                orderRequest.getQuantity()
        );
        return ResponseEntity.ok(orderId);
    }

    @GetMapping("/stocks/ranking")
    public ResponseEntity<List<RankingResponse>> getRanking() {
        log.info("Get ranking request");
        List<RankingResponse> ranking = rankingService.getTopRanking().stream()
                .map(tuple -> new RankingResponse(
                        (String) tuple.getValue(),
                        tuple.getScore()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(ranking);
    }
}
