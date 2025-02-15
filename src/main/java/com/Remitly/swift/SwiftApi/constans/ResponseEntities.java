package com.Remitly.swift.SwiftApi.constans;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public class ResponseEntities {
    public static final ResponseEntity<Map<String, String>> SUCCESS_CREATED =
            ResponseEntity.ok(Map.of("message", "SWIFT code added successfully."));
    public static final ResponseEntity<Map<String, String>> ERROR_FAILED_CREATE  =
            ResponseEntity.badRequest().body(Map.of("message", "Failed to add SWIFT code."));
    public static final ResponseEntity<Map<String, String>> SUCCESS_DELETED  =
            ResponseEntity.ok(Map.of("message", "SWIFT code was deleted successfully."));
    public static final ResponseEntity<Map<String, String>> ERROR_FAILED_DELETE  =
            ResponseEntity.badRequest().body(Map.of("message", "Failed to delete SWIFT code. Swift code doesn't exist."));

}
