package com.example.hlal.controller;

import com.example.hlal.dto.request.TransactionsRequest;
import com.example.hlal.dto.response.TransactionsResponse;
import com.example.hlal.service.TransactionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionsController {

    private final TransactionsService transactionsService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createTransaction(@RequestBody TransactionsRequest request) {
        Map<String, Object> response = new LinkedHashMap<>();

        try {
            TransactionsResponse result = transactionsService.createTransaction(request);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm", new Locale("id", "ID"));
            String formattedDate = result.getTransactionDate().format(formatter);
            result.setTransactionDateFormatted(formattedDate); // pastikan di DTO sudah ada

            response.put("status", true);
            response.put("code", 201);
            response.put("data", result);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException ex) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm", new Locale("id", "ID"));
            response.put("timestamp", LocalDateTime.now().format(formatter));
            response.put("status", false);
            response.put("code", 400);
            response.put("error", "Bad Request");
            response.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getTransactions(
            @RequestParam Long walletId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "desc") String order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(required = false) Integer limit
    ) {
        try {
            Map<String, Object> result = transactionsService.getMyTransactions(
                    walletId, keyword, sortBy, order, page, size, limit
            );
            int statusCode = (int) result.getOrDefault("code", 200);
            return ResponseEntity.status(statusCode).body(result);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new LinkedHashMap<>();
            errorResponse.put("status", false);
            errorResponse.put("code", 500);
            errorResponse.put("message", "Error fetching transactions: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/by-month")
    public ResponseEntity<Map<String, Object>> getTransactionsByMonthAndYear(
            @RequestParam Long walletId,
            @RequestParam int month,
            @RequestParam int year
    ) {
        try {
            Map<String, Object> result = transactionsService.getTransactionsByMonthAndYear(walletId, month, year);
            int statusCode = (int) result.getOrDefault("code", 200);
            return ResponseEntity.status(statusCode).body(result);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new LinkedHashMap<>();
            errorResponse.put("status", false);
            errorResponse.put("code", 500);
            errorResponse.put("message", "Error fetching transactions: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
