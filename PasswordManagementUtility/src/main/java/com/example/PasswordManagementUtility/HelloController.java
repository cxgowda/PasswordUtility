package com.example.PasswordManagementUtility;


import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")   // <-- IMPORTANT
@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/hello")
    public String hello()
    {
        return "Hello from Spring Boot!";
    }

    @GetMapping("/add")
    public int add(@RequestParam int a, @RequestParam int b)
    {
        return a + b;
    }

}
