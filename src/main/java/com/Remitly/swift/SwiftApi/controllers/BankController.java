package com.Remitly.swift.SwiftApi.controllers;

import com.Remitly.swift.SwiftApi.config.ResponseEntities;
import com.Remitly.swift.SwiftApi.models.Bank;
import com.Remitly.swift.SwiftApi.services.BankService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/swift-codes")
public class BankController {
    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping("/{swiftCode}")
    public ResponseEntity<?> showBySwiftCode(@PathVariable String swiftCode){
        return ResponseEntity.ok(bankService.showBySwiftCode(swiftCode));
    }

    @GetMapping("/country/{countryISO2code}")
    public ResponseEntity<?> banksByCountry(@PathVariable String countryISO2code){
        return ResponseEntity.ok(bankService.banksByCountry(countryISO2code));
    }

    @PostMapping
    public ResponseEntity<?> addBank(@RequestBody Bank bank){
        return (bankService.addBank(bank) != null)?
                ResponseEntities.SUCCESS_CREATED : ResponseEntities.ERROR_FAILED_CREATE;
    }

    @DeleteMapping("/{swiftCode}")
    public ResponseEntity<?> deleteBankBySwiftCode(@PathVariable String swiftCode){
        return bankService.deleteBySwiftCode(swiftCode)?
                ResponseEntities.SUCCESS_DELETED : ResponseEntities.ERROR_FAILED_DELETE;

    }

}
