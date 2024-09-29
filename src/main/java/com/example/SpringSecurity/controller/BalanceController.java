package com.example.SpringSecurity.controller;

import com.example.SpringSecurity.model.AccountTransactions;
import com.example.SpringSecurity.repository.AccountTransactionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BalanceController {

    private final AccountTransactionsRepository accountTransactionsRepository;

    @GetMapping("/myBalance")
    public List<AccountTransactions> getBalanceDetails(@RequestParam long id){
        return accountTransactionsRepository.findByCustomerIdOrderByTransactionDtDesc(id);
    }

}
