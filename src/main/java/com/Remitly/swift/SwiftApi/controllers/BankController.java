package com.Remitly.swift.SwiftApi.controllers;

import com.Remitly.swift.SwiftApi.config.ResponseEntities;
import com.Remitly.swift.SwiftApi.models.Bank;
import com.Remitly.swift.SwiftApi.services.BankService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/swift-codes")
public class BankController {
    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping("/{swiftCode}")
    public ResponseEntity<?> showBySwiftCode(@PathVariable String swiftCode){
        Map<String, Object> body = bankService.showBySwiftCode(swiftCode);
        return (body.isEmpty())? ResponseEntity.badRequest().body(body) :
                ResponseEntity.ok(body);
    }

    @GetMapping("/country/{countryISO2code}")
    public ResponseEntity<?> banksByCountry(@PathVariable String countryISO2code) {
        Map<String, Object> body = bankService.banksByCountry(countryISO2code);
        List<?> swiftCodes = (List<?>) body.get("swiftCodes");
        return (swiftCodes.isEmpty())? ResponseEntity.badRequest().body(body) :
                ResponseEntity.ok(body);
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
