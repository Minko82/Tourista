package com.supercoolproject.tourista.service;

import com.supercoolproject.tourista.entity.Event;
import com.supercoolproject.tourista.factory.ServiceInterface;
import com.supercoolproject.tourista.repository.EventRepository;

import java.util.Optional;
import java.util.UUID;

public class EventService implements ServiceInterface {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Optional<Event> getEvent(UUID id) {
        return eventRepository.findById(id);
    }

    // public Event edit(Event event)

    public Event save(Event event) {
        return eventRepository.save(event);
    }

    public void delete(UUID id) {
        eventRepository.deleteById(id);
    }
}
