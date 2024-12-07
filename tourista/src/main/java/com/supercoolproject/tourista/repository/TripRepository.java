package com.supercoolproject.tourista.repository;

import com.supercoolproject.tourista.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

// Repository pattern

/**
 * This interface handles database operations (insert,update,delete...) for us
 * It provides convenient methods to use out of the box, so we don't have to write any SQL
 * However, if we need more custom complex query, we may need to define additional methods
 */
@Repository
public interface TripRepository extends JpaRepository<Trip, UUID> {
}