package com.example.SpringSecurity.controller;

import com.example.SpringSecurity.model.Loans;
import com.example.SpringSecurity.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LoansController {

    private final LoanRepository loanRepository;

    // @PostAuthorize is used to check user access after the method is executed. That is, first the method is executed and then access is checked based on the result.
    // This annotation is usually used when the necessary information to check access is not available until the end of the method execution.
    @GetMapping("/myLoans")
    @PostAuthorize("hasRole('ROOT')")
    public List<Loans> getLoansDetails(@RequestParam long id){
        return loanRepository.findByCustomerIdOrderByStartDtDesc(id);
    }

}
