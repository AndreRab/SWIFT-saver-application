package com.Remitly.swift.SwiftApi.controllers;

import com.Remitly.swift.SwiftApi.constans.ResponseEntities;
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
