package com.nakji.lab.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TestController {

    @GetMapping("test")
    public String test(Model model) {
        model.addAttribute("data", "test!");
        return "test.html";
    }

    @GetMapping("test-mvc")
    public String testMvc(@RequestParam(value = "name", required = false) String name, Model model) {
        model.addAttribute("name", name);
        return "test-mvc.html";
    }
}
