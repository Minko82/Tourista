package com.supercoolproject.tourista.controller;

import com.supercoolproject.tourista.entity.Trip;
import com.supercoolproject.tourista.entity.User;
import com.supercoolproject.tourista.exception.ControllerException;
import com.supercoolproject.tourista.factory.ServiceFactory;
import com.supercoolproject.tourista.factory.ServiceType;
import com.supercoolproject.tourista.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@Controller
public class CalendarController {
    // This annotation means that the method "findUser" will be run for all controller methods in this CalendarController class
    @ModelAttribute("user")
    public User findUser(Principal principal) {
        if (principal.getName() == null) {
            throw new ControllerException(HttpStatus.BAD_REQUEST, "Principal is null!");
        }

        UserService userService = (UserService) ServiceFactory.getService(ServiceType.USER_SERVICE);

        Optional<User> userOptional = userService.findByUsername(principal.getName());

        if (userOptional.isEmpty()) {
            throw new ControllerException(HttpStatus.BAD_REQUEST, "User is not found: " + principal.getName());
        }

        return userOptional.get();
    }

    @GetMapping("/calendar") // URL
    public String calendar(@RequestParam("id") final UUID tripId, final Model model, final User user) {
        // Checks that the trip belongs to the specific user for security--otherwise users will be able to see other
        // user's trips if they have access to the trip id
        Optional<Trip> tripOptional = user.getTrips()
                .stream()
                .filter(userTrip -> tripId.equals(userTrip.getId()))
                .findFirst();

        tripOptional.ifPresent(trip -> model.addAttribute("trip", trip));

        if (tripOptional.isEmpty()) {
            throw new ControllerException(HttpStatus.BAD_REQUEST, "Trip is not found for id " + tripId);
        }

        model.addAttribute("user", user);

        return "trip/calendar";  // view name in resources/templates folder
    }
}