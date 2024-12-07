package com.supercoolproject.tourista.factory;

import com.supercoolproject.tourista.TouristaApplication;
import com.supercoolproject.tourista.repository.EventRepository;
import com.supercoolproject.tourista.repository.TripRepository;
import com.supercoolproject.tourista.repository.UserRepository;
import com.supercoolproject.tourista.service.EventService;
import com.supercoolproject.tourista.service.TripService;
import com.supercoolproject.tourista.service.UserService;

public class ServiceFactory {
    // Autowire cannot support static, so we have to use application context to get the needed methods
    // Application context contains the instances on springboot beans

    // Singleton Pattern:
    private static EventService eventService = null;

    public static EventService getEventServiceInstance() {
        // We have to retrieve the Repository instance from Spring Boot container (IoC) because we cannot create it by ourselves
        EventRepository eventRepository = TouristaApplication.applicationContext.getBean(EventRepository.class); // Looks into Spring Boot container for the instance of EventRepository
        if (eventService == null) {
            eventService = new EventService(eventRepository); // Dependency Injection
        }
        return eventService;
    }

    private static UserService userService = null;

    public static UserService getUserServiceInstance() {
        // We have to retrieve the Repository instance from Spring Boot container (IoC) because we cannot create it by ourselves
        UserRepository userRepository = TouristaApplication.applicationContext.getBean(UserRepository.class); // Looks into Spring Boot container for the instance of EventRepository
        if (userService == null) {
            userService = new UserService(userRepository); // Dependency Injection
        }
        return userService;
    }

    private static TripService tripService = null;

    public static TripService getTripServiceInstance() {
        // We have to retrieve the Repository instance from Spring Boot container (IoC) because we cannot create it by ourselves
        TripRepository tripRepository = TouristaApplication.applicationContext.getBean(TripRepository.class); // Looks into Spring Boot container for the instance of EventRepository
        if (tripService == null) {
            tripService = new TripService(tripRepository); // Dependency Injection
        }
        return tripService;
    }

    // Factory method
    public static ServiceInterface getService(ServiceType serviceType) {
        switch (serviceType) {
            case EVENT_SERVICE -> {
                return getEventServiceInstance();
            }
            case USER_SERVICE -> {
                return getUserServiceInstance();
            }
            case TRIP_SERVICE -> {
                return getTripServiceInstance();
            }
            default -> throw new IllegalStateException("Service Type is not supported.");
        }
    }
}