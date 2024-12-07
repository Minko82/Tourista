package com.supercoolproject.tourista.factory;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceType {
    EVENT_SERVICE("EventService"),
    TRIP_SERVICE("TripService"),
    USER_SERVICE("UserService");

    private final String serviceName;
}