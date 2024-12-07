package com.supercoolproject.tourista.controller;

import com.supercoolproject.tourista.entity.Trip;
import com.supercoolproject.tourista.entity.User;
import com.supercoolproject.tourista.factory.ServiceFactory;
import com.supercoolproject.tourista.factory.ServiceType;
import com.supercoolproject.tourista.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Set;

// Note: https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods

@Controller
public class TripController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/trip/list") // URL
    // Principal is security object in Spring which stores the authenticated user
    public String getTrips(final Model model, final Principal principal) {
        if (principal.getName() == null) {
            return "redirect:error/400";
        }

        UserService userService = (UserService) ServiceFactory.getService(ServiceType.USER_SERVICE);

        // Optional is a wrapper that allows us to check if value is empty
        // It's a better null check that isn't as error prone
        Optional<User> userOptional = userService.findByUsername(principal.getName());
        if (userOptional.isEmpty()) {
            return "redirect:error/default";
        }

        Set<Trip> trips = userOptional.get().getTrips();

        model.addAttribute("trips", trips);
        model.addAttribute("dateFormatter", DateTimeFormatter.ofPattern("MM/dd/yyyy"));

        return "trip/list";  // view name in resources/templates folder
    }

    @GetMapping("/profiles/notifications") // URL
    public String notifications() {
        return "profiles/notifications";  // view name in resources/templates folder
    }

    @GetMapping("/profiles/login") // URL
    public String login() {
        return "profiles/login";  // view name in resources/templates folder
    }

    @GetMapping("/profiles/signup") // URL
    public String signup() {
        return "profiles/signup";  // view name in resources/templates folder
    }

    @GetMapping("/trip/add") // URL
    public String add() {
        return "trip/add";  // view name in resources/templates folder
    }

    @PostMapping("/trip/save") // URL
    public String save(@ModelAttribute Trip newTrip, Principal principal) {
        if (principal.getName() == null) {
            return "redirect:error/400";
        }

        UserService userService = (UserService) ServiceFactory.getService(ServiceType.USER_SERVICE);

        Optional<User> userOptional = userService.findByUsername(principal.getName());

        if (userOptional.isEmpty()) {
            return "redirect:error/default";
        }

        User user = userOptional.get();

        user.addTrip(newTrip);

        userService.save(user);

        return "redirect:/trip/list";  // view name in resources/templates folder
    }
}
