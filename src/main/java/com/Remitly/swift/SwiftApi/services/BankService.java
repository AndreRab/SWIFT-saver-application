package com.Remitly.swift.SwiftApi.services;

import com.Remitly.swift.SwiftApi.models.Bank;
import com.Remitly.swift.SwiftApi.repositories.BankRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankService {
    private final BankRepository bankRepository;

    public BankService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
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
