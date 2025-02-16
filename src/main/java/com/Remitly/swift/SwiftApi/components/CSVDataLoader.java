package com.Remitly.swift.SwiftApi.components;

import com.Remitly.swift.SwiftApi.config.CSVConstants;
import com.Remitly.swift.SwiftApi.models.Bank;
import com.Remitly.swift.SwiftApi.repositories.BankRepository;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class CSVDataLoader {
    @Autowired
    private BankRepository bankRepository;

    @Bean
    CommandLineRunner loadCSV() {
        return args -> {
            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CSVConstants.FILE_NAME)) {
                try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
                    List<String[]> rows = reader.readAll();
                    Map<String, Integer> nameToIndex = convertColumnsNameToIndex(rows.get(0));
                    for (String[] row : rows) {
                        if(canAddBank(row[nameToIndex.get(CSVConstants.SWIFT_CODE)])) {
                            bankRepository.save(new Bank(
                                    row[nameToIndex.get(CSVConstants.ADDRESS)],
                                    row[nameToIndex.get(CSVConstants.BANK_NAME)],
                                    row[nameToIndex.get(CSVConstants.COUNTRY_ISO_2)],
                                    row[nameToIndex.get(CSVConstants.COUNTRY_NAME)],
                                    row[nameToIndex.get(CSVConstants.SWIFT_CODE)].endsWith("XXX"),
                                    row[nameToIndex.get(CSVConstants.SWIFT_CODE)]
                            ));
                        }
                    }
                }
                System.out.println(CSVConstants.SUCCESS_MESSAGE);
            } catch (Exception e) {
                System.out.println(CSVConstants.ERROR_MESSAGE + e.getMessage());
                e.printStackTrace();
            }
        };
    }

    private Map<String, Integer> convertColumnsNameToIndex(String[] headers) {
        return IntStream.range(0, headers.length)
                .boxed()
                .collect(Collectors.toMap(i -> headers[i], Function.identity()));
    }

    private boolean canAddBank(String swiftCode){
        if (!(swiftCode.length() > 7 && swiftCode.length() < 12)){
            return false;
        }
        return bankRepository.findAllBySwiftCode(swiftCode).isEmpty();
    }
}