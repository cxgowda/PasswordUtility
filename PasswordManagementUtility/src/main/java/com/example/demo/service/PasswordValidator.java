/*package com.example.demo.service;


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

*/
package com.example.demo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
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
            issues.add("⚠ Password found in common password list");

        // Check HIBP
        try {
            if (isPwned(password)) {
                issues.add("⚠ Password has been exposed in a data breach (HIBP)");
            }
        } catch (IOException e) {
            issues.add("⚠ Could not check HIBP API");
        }

        if (issues.isEmpty()) issues.add("✅ Password is strong!");
        return issues;
    }

    private static boolean isPwned(String password) throws IOException {
        String sha1 = DigestUtils.sha1Hex(password).toUpperCase();
        String prefix = sha1.substring(0, 5);
        String suffix = sha1.substring(5);

        URL url = new URL("https://api.pwnedpasswords.com/range/" + prefix);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "PasswordValidator");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts[0].equalsIgnoreCase(suffix)) {
                    return true;
                }
            }
        }
        return false;
    }
}

