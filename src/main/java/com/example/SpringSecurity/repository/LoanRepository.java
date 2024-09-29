package com.example.SpringSecurity.repository;

import com.example.SpringSecurity.model.Loans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loans, Long> {

    List<Loans> findByCustomerIdOrderByStartDtDesc(long customerId);

}
