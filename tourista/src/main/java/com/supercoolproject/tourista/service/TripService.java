package com.supercoolproject.tourista.service;

import com.supercoolproject.tourista.entity.Trip;
import com.supercoolproject.tourista.factory.ServiceInterface;
import com.supercoolproject.tourista.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TripService implements ServiceInterface {
    @Autowired
    private final TripRepository tripRepository;

    @Autowired
    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    // Pagination
    public Page<Trip> getTrips(final int pageNumber, final int pageSize) {
        return tripRepository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    public List<Trip> getTrips() {
        return tripRepository.findAll();
    }

    public Optional<Trip> getTrip(UUID id) {
        return tripRepository.findById(id);
    }

    public Trip save(Trip trip) {
        return tripRepository.save(trip);
    }

    public void delete(UUID id) {
        tripRepository.deleteById(id);
    }
}