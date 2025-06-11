package com.grupo12.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.grupo12.models.FAQ;
import com.grupo12.services.implementation.FAQService;

@Controller
@RequestMapping("/client")
public class HelpController {

    @GetMapping("/help")
    public String verAyuda(Model model, @RequestParam(value = "query", required = false) String query) {
        List<FAQ> faqs = FAQService.getAll(); // Simulado
        if (query != null && !query.isEmpty()) {
            faqs = faqs.stream()
                    .filter(f -> f.getRequest().toLowerCase().contains(query.toLowerCase()) ||
                                 f.getResponse().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
        }
        model.addAttribute("faqs", faqs);
        model.addAttribute("query", query);
        return "client/help";
    }
}
