package com.example.SpringSecurity.controller;

import com.example.SpringSecurity.model.Loans;
import com.example.SpringSecurity.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LoansController {

    private final LoanRepository loanRepository;

    @GetMapping("/myLoans")
    public List<Loans> getLoansDetails(@RequestParam long id){
        return loanRepository.findByCustomerIdOrderByStartDtDesc(id);
    }

}
