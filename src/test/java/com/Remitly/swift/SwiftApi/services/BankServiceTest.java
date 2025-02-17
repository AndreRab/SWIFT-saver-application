package com.Remitly.swift.SwiftApi.services;

import com.Remitly.swift.SwiftApi.models.Bank;
import com.Remitly.swift.SwiftApi.repositories.BankRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mockito;

import java.util.List;
import java.util.Map;
//import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BankServiceTest {

    private BankRepository bankRepository;
    private BankService bankService;

    @BeforeEach
    void setUp() {
        bankRepository = mock(BankRepository.class);
        bankService = new BankService(bankRepository);
    }

    @Test
    void showBySwiftCodeBankNotFound() {
        when(bankRepository.findAllBySwiftCode("UNKNOWN")).thenReturn(List.of());

        Map<String, Object> result = bankService.showBySwiftCode("UNKNOWN");

        assertTrue(result.isEmpty());
        verify(bankRepository).findAllBySwiftCode("UNKNOWN");
    }

    @Test
    void showBySwiftCodeHeadquarter() {
        Bank headquarter = new Bank("Address HQ", "Head Bank", "PL", "Poland", true, "PLSWIFT1XXX");
        when(bankRepository.findAllBySwiftCode("PLSWIFT1XXX")).thenReturn(List.of(headquarter));

        Bank branch1 = new Bank("Address B1", "Branch1", "PL", "Poland", false, "PLSWIFT1ABC");
        Bank branch2 = new Bank("Address B2", "Branch2", "PL", "Poland", false, "PLSWIFT1DEF");

        when(bankRepository.findAll()).thenReturn(List.of(headquarter, branch1, branch2));

        Map<String, Object> result = bankService.showBySwiftCode("PLSWIFT1XXX");

        assertEquals("PL", result.get("countryISO2"));
        assertTrue(result.containsKey("branches"));
        assertEquals(3, ((List<?>) result.get("branches")).size());
    }

    @Test
    void showBySwiftCodeBranch() {
        Bank branch = new Bank("Branch Addr", "Branch Bank", "US", "USA", false, "USSWIFT1XXX");
        when(bankRepository.findAllBySwiftCode("USSWIFT1XXX")).thenReturn(List.of(branch));

        Map<String, Object> result = bankService.showBySwiftCode("USSWIFT1XXX");

        assertEquals(branch.asMap(true), result);
        assertFalse(result.containsKey("branches"));
    }

    @Test
    void banksByCountryBankNotFound() {
        when(bankRepository.findAllByCountryISO2("DE")).thenReturn(List.of());

        Map<String, Object> result = bankService.banksByCountry("DE");

        assertEquals("DE", result.get("countryISO2"));
        assertEquals("DE", result.get("countryName"));
        assertTrue(((List<?>) result.get("swiftCodes")).isEmpty());
    }

    @Test
    void banksByCountry() {
        Bank bank1 = new Bank("Addr1", "Bank1", "IT", "Italy", false, "ITSWIFT1XXX");
        Bank bank2 = new Bank("Addr2", "Bank2", "IT", "Italy", false, "ITSWIFT2XXX");

        when(bankRepository.findAllByCountryISO2("IT")).thenReturn(List.of(bank1, bank2));

        Map<String, Object> result = bankService.banksByCountry("IT");

        assertEquals("IT", result.get("countryISO2"));
        assertEquals("ITALY", result.get("countryName"));
        assertEquals(2, ((List<?>) result.get("swiftCodes")).size());
    }

    @Test
    void addBankInvalidSwiftCode() {
        Bank invalidBank = new Bank("Addr", "InvalidBank", "PL", "Poland", false, "PL1");

        Bank result = bankService.addBank(invalidBank);

        assertNull(result);
        verify(bankRepository, never()).save(any());
    }

    @Test
    void addBankDuplicateSwiftCode() {
        Bank bank = new Bank("Addr", "Bank", "PL", "Poland", false, "PLSWIFT1XXX");
        when(bankRepository.findAllBySwiftCode("PLSWIFT1XXX")).thenReturn(List.of(bank));

        Bank result = bankService.addBank(bank);

        assertNull(result);
        verify(bankRepository, never()).save(bank);
    }

    @Test
    void addBank() {
        Bank validBank = new Bank("Addr", "Bank", "FR", "France", false, "FRSWIFT1XXX");
        when(bankRepository.findAllBySwiftCode("FRSWIFT1XXX")).thenReturn(List.of());
        when(bankRepository.save(validBank)).thenReturn(validBank);

        Bank result = bankService.addBank(validBank);

        assertNotNull(result);
        assertEquals(validBank, result);
        verify(bankRepository).save(validBank);
    }

    @Test
    void deleteBySwiftCodeNotFound() {
        when(bankRepository.findAllBySwiftCode("NONEXISTENT")).thenReturn(List.of());

        boolean result = bankService.deleteBySwiftCode("NONEXISTENT");

        assertFalse(result);
        verify(bankRepository, never()).delete(any());
    }

    @Test
    void deleteBySwiftCode() {
        Bank bank = new Bank("Addr", "Bank", "ES", "Spain", false, "ESSWIFT1XXX");
        when(bankRepository.findAllBySwiftCode("ESSWIFT1XXX")).thenReturn(List.of(bank));

        boolean result = bankService.deleteBySwiftCode("ESSWIFT1XXX");

        assertTrue(result);
        verify(bankRepository).delete(bank);
    }
}

