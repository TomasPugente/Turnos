package com.grupo12.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.grupo12.helpers.ViewRouteHelper;


@Controller
@RequestMapping("/Login")
public class Login {

    @GetMapping("")
    public String login(Model model,
                        @RequestParam(name="error", required=false) String error,
                        @RequestParam(name="logout", required=false) String logout) {
        model.addAttribute("error", error);
        model.addAttribute("logout", logout);
        return ViewRouteHelper.LOGIN;
    }

    @GetMapping("/Register")
    public String getMethodName(@RequestParam String param) {
        return ViewRouteHelper.REGISTER;
    }
    
}
