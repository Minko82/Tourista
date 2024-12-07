package com.supercoolproject.tourista.controller;

import com.supercoolproject.tourista.entity.User;
import com.supercoolproject.tourista.factory.ServiceFactory;
import com.supercoolproject.tourista.factory.ServiceType;
import com.supercoolproject.tourista.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/profiles/signup")
    public String createAccount(@ModelAttribute final User user, final BindingResult bindingResult) {
        // if username is found in db
        // we want to add an error to model
        // return back to the 'signup' view

        UserService userService = (UserService) ServiceFactory.getService(ServiceType.USER_SERVICE);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userService.save(user);

        return "redirect:/login";
    }

    @GetMapping("/profile") // URL
    public String profile() {
        return "profiles/profile";  // view name in resources/templates folder
    }
}