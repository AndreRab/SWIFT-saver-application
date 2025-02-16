package com.Remitly.swift.SwiftApi.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

import java.util.Map;

@Entity
public class Bank {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    private String address;
    @Getter
    private String bankName;
    @Getter
    private String countryISO2;
    @Getter
    private String countryName;
    @Getter
    private boolean isHeadquarter;
    @Getter
    private String swiftCode;

    public void setId(Long id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setCountryISO2(String countryISO2) {
        this.countryISO2 = countryISO2.toUpperCase();
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName.toUpperCase();
    }

    public void setHeadquarter(boolean headquarter) {
        isHeadquarter = headquarter;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public Bank(Long id, String address, String bankName, String countryISO2, String countryName, boolean isHeadquarter, String swiftCode) {
        this.id = id;
        this.address = address;
        this.bankName = bankName;
        this.countryISO2 = countryISO2.toUpperCase();
        this.countryName = countryName.toUpperCase();
        this.isHeadquarter = isHeadquarter;
        this.swiftCode = swiftCode;
    }

    public Bank(String address, String bankName, String countryISO2, String countryName, boolean isHeadquarter, String swiftCode) {
        this.address = address;
        this.bankName = bankName;
        this.countryISO2 = countryISO2.toUpperCase();
        this.countryName = countryName.toUpperCase();
        this.isHeadquarter = isHeadquarter;
        this.swiftCode = swiftCode;
    }

    public Bank() {}

    public Map<String, Object> asMap(boolean withCountryName){
        Map<String, Object> map =  Map.of(
                "address", this.address,
                "bankName", this.bankName,
                "countryISO2", this.countryISO2,
                "countryName", this.countryName,
                "isHeadquarter", this.isHeadquarter,
                "swiftCode", this.swiftCode
        );

        if(withCountryName){
            map = (Map<String, Object>) map.remove("countryName");
        }
        return map;
    }
}
