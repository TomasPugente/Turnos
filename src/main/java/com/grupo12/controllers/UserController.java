package com.grupo12.controllers;

import java.util.UUID;

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

import com.grupo12.entities.Client;
import com.grupo12.entities.Contact;
import com.grupo12.entities.User;
import com.grupo12.entities.UserRole;
import com.grupo12.helpers.ViewRouteHelper;
import com.grupo12.models.ClientDTO;
import com.grupo12.services.implementation.ClientService;
import com.grupo12.services.implementation.EmailService;
import com.grupo12.services.implementation.UserService;

import jakarta.validation.Valid;

@Controller
public class UserController {

    private final ClientService clientService;
    @Autowired
    private final UserService userService;

    @Autowired
    private EmailService emailService;

    public UserController(UserService userService, ClientService clientService) {
        this.userService = userService;
        this.clientService = clientService;
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
    public ModelAndView register() {
        ModelAndView mv = new ModelAndView();
        mv.addObject("client", new ClientDTO());
        mv.setViewName(ViewRouteHelper.USER_REGISTER);
        System.out.println("getRegister");
        return mv;
    }

    @PostMapping("/registeruser")
    public ModelAndView registeruser(@Valid @ModelAttribute("client") ClientDTO clientDTO,
            BindingResult bindingResult) {
        ModelAndView mv = new ModelAndView(ViewRouteHelper.USER_REGISTER);
        try {

            if (bindingResult.hasErrors()) {
                return mv;
            }

            if (userService.existsByUsername(clientDTO.getUser().getUsername())) {
                mv.addObject("error", "Username is already in use.");
                return mv;
            }

            if (userService.existsByEmail(clientDTO.getUser().getEmail())) {
                mv.addObject("error", "Email is already in use.");
                return mv;
            }

            Client client = new Client();

            client.setName(clientDTO.getName());
            client.setSurname(clientDTO.getSurname());
            client.setDni(clientDTO.getDni());
            client.setDateOfBirth(clientDTO.getDateOfBirth());

            Contact contact = new Contact();
            contact.setPhone(clientDTO.getContact().getPhone());
            contact.setEmail(clientDTO.getUser().getEmail());

            User user = new User();
            user.setUsername(clientDTO.getUser().getUsername());
            user.setEmail(clientDTO.getUser().getEmail());
            user.setPassword(clientDTO.getUser().getPassword());
            user.setEnabled(true);

            UserRole defaultRole = new UserRole(user, "ROLE_USER");
            user.getUserRoles().add(defaultRole);
            client.setUser(user);
            clientService.save(client);

            emailService.sendSimpleMail(user.getEmail(), "Correo de prueba",
                    "If you are receiving this email, it means that your registration was successful. Welcome "
                            + client.getName() + " " + client.getSurname() + " to our platform!");
            System.out.println("Correo enviado correctamente");
            mv.setViewName("redirect:/login?registered=true");
        } catch (Exception e) {
            mv.addObject("error", "A problem has been ocurred: " + e.getMessage());

        }
        return mv;
    }

    @GetMapping("/forgotpassword")
    public String forgotpassword() {
        return ViewRouteHelper.FORGOT_PASSWORD;
    }

    @PostMapping("/forgotpassword")
    public String forgotPasswordSubmit(@RequestParam("email") String email, Model model) {
        try {

            User user = userService.findByEmail(email);
            if (user == null) {
                model.addAttribute("error", "No user found with that email address.");
                return ViewRouteHelper.FORGOT_PASSWORD;
            }

            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            userService.save(user);

            String resetLink = "http://localhost:8080/resetpassword?token=" + token;
            System.out.println("Reset link: " + resetLink);
            String subject = "Reset your password";
            String message = "Hi " + user.getUsername() + ",\n\n"
                    + "To reset your password, click the link below:\n"
                    + resetLink + " \n\n"
                    + "If you did not request this, you can ignore this email.";
            System.out.println("Sending email to: " + user.getEmail());
            emailService.sendSimpleMail(user.getEmail(), subject, message);
            System.out.println("Email sent successfully to " + user.getEmail());
            model.addAttribute("success", "A email has been sent to " + user.getEmail()
                    + " with instructions to reset your password.");
        } catch (Exception e) {
            model.addAttribute("error", "A problem has occurred: " + e.getMessage());
        }
        return ViewRouteHelper.FORGOT_PASSWORD;
    }

    @GetMapping("/resetpassword")
    public String resetpassword(@RequestParam("token") String token, Model model) {
        User user = userService.findByResetToken(token);
        if (user == null) {
            model.addAttribute("error", "Token inválido o expirado.");
        } else {

            model.addAttribute("token", token);
        }

        return ViewRouteHelper.USER_RESET_PASSWORD;
    }

    @PostMapping("/resetpassword")
    public String processReset(
            @RequestParam("token") String token,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Las contraseñas no coinciden.");
            model.addAttribute("token", token);
            return ViewRouteHelper.USER_RESET_PASSWORD;
        }

        User user = userService.findByResetToken(token);
        if (user == null) {
            model.addAttribute("error", "Token inválido o expirado.");
            return ViewRouteHelper.USER_RESET_PASSWORD;
        }

        user.setPassword(password);
        user.setResetToken(null);
        userService.save(user);

        model.addAttribute("success", "Contraseña restablecida correctamente. Iniciá sesión.");
        return "redirect:/login?resetSuccess";
    }
}
