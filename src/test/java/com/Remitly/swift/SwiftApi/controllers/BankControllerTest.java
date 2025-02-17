package com.Remitly.swift.SwiftApi.controllers;

import com.Remitly.swift.SwiftApi.config.ResponseEntities;
import com.Remitly.swift.SwiftApi.models.Bank;
import com.Remitly.swift.SwiftApi.services.BankService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BankControllerTest {

    private BankService bankService;
    private BankController bankController;

    @BeforeEach
    void setUp() {
        bankService = mock(BankService.class);
        bankController = new BankController(bankService);
    }

    @Test
    void showBySwiftCode() {
        Map<String, Object> bankInfo = Map.of("swiftCode", "PLSWIFT1XXX", "bankName", "Bank Poland");
        when(bankService.showBySwiftCode("PLSWIFT1XXX")).thenReturn(bankInfo);

        ResponseEntity<?> response = bankController.showBySwiftCode("PLSWIFT1XXX");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(bankInfo, response.getBody());
        verify(bankService).showBySwiftCode("PLSWIFT1XXX");
    }

    @Test
    void showBySwiftCodeBankNotFound() {
        when(bankService.showBySwiftCode("UNKNOWN")).thenReturn(Collections.emptyMap());

        ResponseEntity<?> response = bankController.showBySwiftCode("UNKNOWN");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Collections.emptyMap(), response.getBody());
        verify(bankService).showBySwiftCode("UNKNOWN");
    }

    @Test
    void banksByCountry() {
        Map<String, Object> countryBanks = Map.of("countryISO2", "PL", "swiftCodes", Collections.singletonList(Map.of("swiftCode", "PLSWIFT1XXX")));
        when(bankService.banksByCountry("PL")).thenReturn(countryBanks);

        ResponseEntity<?> response = bankController.banksByCountry("PL");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(countryBanks, response.getBody());
        verify(bankService).banksByCountry("PL");
    }

    @Test
    void banksByCountryWhenNoBanks() {
        when(bankService.banksByCountry("DE")).thenReturn(Collections.emptyMap());

        ResponseEntity<?> response = bankController.banksByCountry("DE");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Collections.emptyMap(), response.getBody());
        verify(bankService).banksByCountry("DE");
    }

    @Test
    void addBank() {
        Bank validBank = new Bank("Addr", "BankName", "PL", "Poland", false, "PLSWIFT1XXX");
        when(bankService.addBank(validBank)).thenReturn(validBank);

        ResponseEntity<?> response = bankController.addBank(validBank);

        assertEquals(ResponseEntities.SUCCESS_CREATED.getStatusCodeValue(), response.getStatusCodeValue());
        assertEquals(ResponseEntities.SUCCESS_CREATED.getBody(), response.getBody());
        verify(bankService).addBank(validBank);
    }

    @Test
    void addBankFail() {
        Bank invalidBank = new Bank("Addr", "BankName", "PL", "Poland", false, "PLSWIFT1XXX");
        when(bankService.addBank(invalidBank)).thenReturn(null);

        ResponseEntity<?> response = bankController.addBank(invalidBank);

        assertEquals(ResponseEntities.ERROR_FAILED_CREATE.getStatusCodeValue(), response.getStatusCodeValue());
        assertEquals(ResponseEntities.ERROR_FAILED_CREATE.getBody(), response.getBody());
        verify(bankService).addBank(invalidBank);
    }

    @Test
    void deleteBankBySwiftCode() {
        when(bankService.deleteBySwiftCode("PLSWIFT1XXX")).thenReturn(true);

        ResponseEntity<?> response = bankController.deleteBankBySwiftCode("PLSWIFT1XXX");

        assertEquals(ResponseEntities.SUCCESS_DELETED.getStatusCodeValue(), response.getStatusCodeValue());
        assertEquals(ResponseEntities.SUCCESS_DELETED.getBody(), response.getBody());
        verify(bankService).deleteBySwiftCode("PLSWIFT1XXX");
    }

    @Test
    void deleteBankBySwiftCodeFail() {
        when(bankService.deleteBySwiftCode("NONEXISTENT")).thenReturn(false);

        ResponseEntity<?> response = bankController.deleteBankBySwiftCode("NONEXISTENT");

        assertEquals(ResponseEntities.ERROR_FAILED_DELETE.getStatusCodeValue(), response.getStatusCodeValue());
        assertEquals(ResponseEntities.ERROR_FAILED_DELETE.getBody(), response.getBody());
        verify(bankService).deleteBySwiftCode("NONEXISTENT");
    }
}
