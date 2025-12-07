package com.example.demo.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PasswordValidator {

    private static List<String> commonPasswords = new ArrayList<>();

    static {
        // Load JSON file from resources
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = PasswordValidator.class.getResourceAsStream("/common-passwords.json")) {
            if (is != null) {
                commonPasswords = mapper.readValue(is, new TypeReference<List<String>>() {});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static List<String> validatePassword(String password) {
        List<String> issues = new ArrayList<>();

        if (password.length() < 12) issues.add("❌ Password too short (min 12 chars)");
        if (!password.matches(".*[A-Z].*")) issues.add("❌ Must contain uppercase letter");
        if (!password.matches(".*[a-z].*")) issues.add("❌ Must contain lowercase letter");
        if (!password.matches(".*\\d.*")) issues.add("❌ Must contain a number");
        if (!password.matches(".*[@#$%^&+=!].*")) issues.add("❌ Must contain a special char (@#$%^&+=!)");
        if (commonPasswords.contains(password.toLowerCase()))
            issues.add("⚠ Password Found in Data breach");

        if (issues.isEmpty()) issues.add("✅ Password is strong!");
        return issues;
    }
}
