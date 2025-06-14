package com.grupo12.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.grupo12.entities.User;
import com.grupo12.helpers.ViewRouteHelper;
import com.grupo12.models.UserDTO;
import com.grupo12.services.implementation.UserService;

import jakarta.validation.Valid;

@Controller
public class UserController {
    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(Model model,
            @RequestParam(name = "error", required = false) String error,
            @RequestParam(name = "logout", required = false) String logout,
            @RequestParam(name = "registered", required = false) String registered) {

        model.addAttribute("error", error);
        model.addAttribute("logout", logout);
        model.addAttribute("registered", registered != null);
        return ViewRouteHelper.USER_LOGIN;
    }

    @GetMapping("/logout")
    public String logout() {
        return ViewRouteHelper.USER_LOGOUT;
    }

    @GetMapping("/loginsuccess")
    public String loginCheck() {
        return "redirect:/index";
    }

    @GetMapping("/registeruser")
    public String registeruser() {
        return ViewRouteHelper.USER_REGISTERUSER;
    }

    @GetMapping("/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView(ViewRouteHelper.ROUTE_INDEX);
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        modelAndView.addObject("username", user.getUsername());
        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView showRegisterForm() {
        ModelAndView mv = new ModelAndView();
        mv.addObject("user", new UserDTO());
        mv.setViewName(ViewRouteHelper.USER_REGISTER);
        System.out.println("getRegister");
        return mv;
    }

    @PostMapping("/registeruser")
    public ModelAndView registeruser(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult) {
        ModelAndView mv = new ModelAndView(ViewRouteHelper.USER_REGISTER);
        try {

            System.out.println("Intentando registrar usuario");

            if (bindingResult.hasErrors()) {
                return mv;
            }

            if (userService.existsByUsername(userDTO.getUsername())) {
                mv.addObject("error", "El nombre de usuario ya está en uso.");
                return mv;
            }

            if (userService.existsByEmail(userDTO.getEmail())) {
                mv.addObject("error", "El correo electrónico ya está en uso.");
                return mv;
            }

            User user = new User();
            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword());
            user.setEnabled(true);
            System.out.println("Registrando usuario: " + user.getUsername());
            userService.save(user);

            mv.setViewName("redirect:/login?registered=true");
        } catch (Exception e) {
            mv.addObject("error", "Error al registrar el usuario: " + e.getMessage());

        }
        return mv;
    }

    @GetMapping("/forgotpassword")
    public String forgotpassword() {
        return ViewRouteHelper.FORGOT_PASSWORD;
    }
}
