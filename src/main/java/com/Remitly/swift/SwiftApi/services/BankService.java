package com.Remitly.swift.SwiftApi.services;

import com.Remitly.swift.SwiftApi.config.BankServiceLoggers;
import com.Remitly.swift.SwiftApi.config.CSVConstants;
import com.Remitly.swift.SwiftApi.models.Bank;
import com.Remitly.swift.SwiftApi.repositories.BankRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BankService {
    private static final Logger logger = LoggerFactory.getLogger(BankService.class);
    private final BankRepository bankRepository;

    public BankService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public Map<String, Object> showBySwiftCode(String swiftCode) {
        List<Bank> bankList = bankRepository.findAllBySwiftCode(swiftCode);
        if(bankList.isEmpty()){
            logger.warn(BankServiceLoggers.NO_BANK_SWIFT_CODE, swiftCode);
            return Map.of();
        }
        Bank bank = bankList.get(0);
        return bank.isHeadquarter()? showHeadquarterBySwiftCode(bank) : showBranchBySwiftCode(bank);
    }

    private Map<String, Object> showHeadquarterBySwiftCode(Bank bank){
        logger.info(BankServiceLoggers.HEAD_QUARTER_MESSAGE, bank.getSwiftCode());
        Map<String, Object> map = bank.asMap(true);
        String branchCode = bank.getSwiftCode().substring(0, 8);
        List<Bank> branchesBank = bankRepository.findAll().
                stream().
                filter(b -> b.getSwiftCode().substring(0, 8).equals(branchCode)).
                toList();
        map.put("branches", branchesBank.
                        stream().
                        map(b -> b.asMap(false)).
                        collect(Collectors.toList())
                );
        return map;
    }

    private Map<String, Object> showBranchBySwiftCode(Bank bank){
        logger.info(BankServiceLoggers.BRANCH_MESSAGE, bank.getSwiftCode());
        return bank.asMap(true);
    }

    public Map<String, Object> banksByCountry(String countryCode) {
        logger.info(BankServiceLoggers.COUNTRY_MESSAGE, countryCode);
        List<Bank> banks = bankRepository.findAllByCountryISO2(countryCode);

        if(banks.isEmpty()){
            logger.warn(BankServiceLoggers.NO_BANK_COUNTRY, countryCode);
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
                || !(bank.getSwiftCode().length() >= CSVConstants.MIN_SWIFT_CODE_LENGTH && bank.getSwiftCode().length() <= CSVConstants.MAX_SWIFT_CODE_LENGTH)){
            logger.warn(BankServiceLoggers.INVALID_SWIFT_CODE, bank.getSwiftCode());
            return false;
        }
        if(!bankRepository.findAllBySwiftCode(bank.getSwiftCode()).isEmpty()){
            logger.warn(BankServiceLoggers.BANK_EXISTS, bank.getSwiftCode());
            return false;
        }
        return true;
    }

    public Bank addBank(Bank bank){
        logger.info(BankServiceLoggers.ADD_BANK_MESSAGE, bank);
        return canAddBank(bank)? bankRepository.save(bank) : null;
    }

    public boolean deleteBySwiftCode(String swiftCode){
        logger.info(BankServiceLoggers.DELETE_BANK_MESSAGE, swiftCode);
        List<Bank> bankAsList = bankRepository.findAllBySwiftCode(swiftCode);
        if(bankAsList.isEmpty()){
            logger.warn(BankServiceLoggers.NO_BANK_SWIFT_CODE, swiftCode);
            return false;
        }
        bankRepository.delete(bankAsList.get(0));
        return true;
    }
}
