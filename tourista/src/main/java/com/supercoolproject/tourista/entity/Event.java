package com.supercoolproject.tourista.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event")
public class Event {

    // UUID is a unique ID generator
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String location;

    // ZonedDateTime is a datatype for international time
    private LocalDateTime startDateTime; // includes time
    private LocalDateTime endDateTime;

    private Double estimatedCost;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "trip_id")
    private Trip trip;
}
