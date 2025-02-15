package com.Remitly.swift.SwiftApi.repositories;

import com.Remitly.swift.SwiftApi.models.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankRepository extends JpaRepository<Bank, Long> {
    <B extends Bank> B save(B bank);

    List<Bank> findAllBySwiftCode(String swiftCode);

}
