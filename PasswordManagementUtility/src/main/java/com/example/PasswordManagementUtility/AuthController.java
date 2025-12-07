package com.example.PasswordManagementUtility;

import com.example.demo.service.PasswordValidator;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class AuthController {

    private Map<String, String> users = new HashMap<>();

    @PostMapping("/signup")
    public String signup(@RequestParam String user,
                         @RequestParam String pass,
                         @RequestParam String confirm) {

        if (!pass.equals(confirm)) return "❌ Passwords do not match!";

        List<String> issues = PasswordValidator.validatePassword(pass);
        if (!issues.get(0).startsWith("✅")) return String.join("\n", issues);

        if (users.containsKey(user)) return "❌ Username already exists!";

        users.put(user, pass);
        return "✅ Signup Successful!";
    }

    @PostMapping("/login")
    public String login(@RequestParam String user,
                        @RequestParam String pass) {

        if (users.containsKey(user) && users.get(user).equals(pass)) {
            return "✔ Login Successful!";
        }
        return "❌ Invalid Username or Password!";
    }




}
