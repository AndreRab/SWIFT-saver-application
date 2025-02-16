package com.Remitly.swift.SwiftApi.services;

import com.Remitly.swift.SwiftApi.models.Bank;
import com.Remitly.swift.SwiftApi.repositories.BankRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BankService {
    private final BankRepository bankRepository;

    public BankService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public Map<String, Object> showBySwiftCode(String swiftCode) {
        List<Bank> bankList = bankRepository.findAllBySwiftCode(swiftCode);
        if(bankList.isEmpty()){
            return Map.of();
        }
        Bank bank = bankList.get(0);
        if(bank.isHeadquarter()){
            return showHeadquarterBySwiftCode(bank);
        }
        return showBranchBySwiftCode(bank);
    }

    private Map<String, Object> showHeadquarterBySwiftCode(Bank bank){
        Map<String, Object> map = bank.asMap(true);
        String branchCode = bank.getSwiftCode().substring(0, 8);
        List<Bank> branchesBank = bankRepository.findAll().
                stream().
                filter(b -> b.getSwiftCode().substring(0, 8).equals(branchCode)).
                collect(Collectors.toList());
        map.put("branches", branchesBank.
                        stream().
                        map(b -> b.asMap(false)).
                        collect(Collectors.toList())
                );
        return map;
    }

    private Map<String, Object> showBranchBySwiftCode(Bank bank){
        return bank.asMap(true);
    }

    public Map<String, Object> banksByCountry(String countryCode) {
        List<Bank> banks = bankRepository.findAllByCountryISO2(countryCode);

        if(banks.isEmpty()){
            return Map.of(
                    "countryISO2", countryCode,
                    "countryName", countryCode,
                    "swiftCodes", new ArrayList<>()
            );
        }

        return Map.of(
                "countryISO2", banks.get(0).getCountryISO2(),
                "countryName", banks.get(0).getCountryName(),
                "swiftCodes", banks.stream().
                        map(bank -> bank.asMap(false)).
                        collect(Collectors.toList())
        );
    }

    private boolean canAddBank(Bank bank){
        if (bank.getSwiftCode() == null
                || !(bank.getSwiftCode().length() > 7 && bank.getSwiftCode().length() < 12)){
            return false;
        }
        return bankRepository.findAllBySwiftCode(bank.getSwiftCode()).isEmpty();
    }

    public Bank addBank(Bank bank){
        if (!canAddBank(bank)){
            return null;
        }
        return bankRepository.save(bank);
    }

    public boolean deleteBySwiftCode(String swiftCode){
        List<Bank> bankAsList = bankRepository.findAllBySwiftCode(swiftCode);
        if(bankAsList.isEmpty()){
            return false;
        }
        bankRepository.delete(bankAsList.get(0));
        return true;
    }
}
