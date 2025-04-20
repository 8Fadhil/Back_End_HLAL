package com.example.hlal.repository;

import com.example.hlal.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Long> {
    @Query("SELECT t FROM Transactions t WHERE t.wallet.id = :walletId AND t.transactionDate BETWEEN :startDate AND :endDate ORDER BY t.transactionDate DESC")
    List<Transactions> findByWalletIdAndTransactionDateBetween(Long walletId, LocalDateTime startDate, LocalDateTime endDate);
    List<Transactions> findByWalletId(Long walletId);
    List<Transactions> findByRecipientWalletId(Long recipientWalletId);
    List<Transactions> findByTransactionTypeId(Long transactionTypeId);
}
