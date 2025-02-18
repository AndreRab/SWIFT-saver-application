package com.Remitly.swift.SwiftApi.components;

import com.Remitly.swift.SwiftApi.config.CSVConstants;
import com.Remitly.swift.SwiftApi.models.Bank;
import com.Remitly.swift.SwiftApi.repositories.BankRepository;
import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
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
    private static final Logger logger = LoggerFactory.getLogger(CSVDataLoader.class);
    private final Object lock = new Object();

    @Bean
    CommandLineRunner loadCSV() {
        return args -> {
            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CSVConstants.FILE_NAME)) {
                try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
                    List<String[]> rows = reader.readAll();
                    Map<String, Integer> nameToIndex = convertColumnsNameToIndex(rows.get(0));
                    for (String[] row : rows) {
                        synchronized (lock) {
                            addBank(row, nameToIndex);
                        }
                    }
                }
                logger.info(CSVConstants.SUCCESS_MESSAGE);
            } catch (Exception e) {
                logger.error("{}: {}", CSVConstants.ERROR_MESSAGE, e.getMessage(), e);
            }
        };
    }

    private void addBank(String row[], Map<String, Integer> nameToIndex){
        if (canAddBank(row[nameToIndex.get(CSVConstants.SWIFT_CODE)])) {
            try {
                bankRepository.save(new Bank(
                        row[nameToIndex.get(CSVConstants.ADDRESS)],
                        row[nameToIndex.get(CSVConstants.BANK_NAME)],
                        row[nameToIndex.get(CSVConstants.COUNTRY_ISO_2)],
                        row[nameToIndex.get(CSVConstants.COUNTRY_NAME)],
                        row[nameToIndex.get(CSVConstants.SWIFT_CODE)].endsWith("XXX"),
                        row[nameToIndex.get(CSVConstants.SWIFT_CODE)]
                ));
            } catch (DataIntegrityViolationException e) {
                logger.error("{}: {}", CSVConstants.DUPLICATE_MESSAGE, e.getMessage(), e);
            }
        }
    }

    private Map<String, Integer> convertColumnsNameToIndex(String[] headers) {
        return IntStream.range(0, headers.length)
                .boxed()
                .collect(Collectors.toMap(i -> headers[i], Function.identity()));
    }

    private boolean canAddBank(String swiftCode){
        if (swiftCode == null || swiftCode.isBlank() || swiftCode.length() < CSVConstants.MIN_SWIFT_CODE_LENGTH || swiftCode.length() > CSVConstants.MAX_SWIFT_CODE_LENGTH) {
            return false;
        }
        return !bankRepository.existsBySwiftCode(swiftCode);
    }

}