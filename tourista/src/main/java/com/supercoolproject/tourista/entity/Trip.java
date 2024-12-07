package com.supercoolproject.tourista.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
//@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trip")
public class Trip {
    // UUID is a unique ID generator
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String destination;

    // ZonedDateTime is a datatype for international time
    private LocalDate startDate;
    private LocalDate endDate;

    private Double estimatedCost;

    // @ToString.Exclude and @EqualsAndHashCode.Exclude prevent circular reference due to Lombok auto-generated methods
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference  // initite loop for bi-directional association: https://stackoverflow.com/questions/3325387/infinite-recursion-with-jackson-json-and-hibernate-jpa-issue/18288939#18288939
    private User user;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private final Set<Event> events = new HashSet<>();

    public void addEvent(Event event) {
        this.events.add(event);
        event.setTrip(this);
    }

    public static TripBuilder builder() {
        return new TripBuilder();
    }

    public static class TripBuilder {
        private UUID id;
        private String name;
        private String destination;
        private LocalDate startDate;
        private LocalDate endDate;
        private Double estimatedCost;
        private User user;

        TripBuilder() {
        }

        public TripBuilder id(final UUID id) {
            this.id = id;
            return this;
        }

        public TripBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public TripBuilder destination(final String destination) {
            this.destination = destination;
            return this;
        }

        public TripBuilder startDate(final LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public TripBuilder endDate(final LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public TripBuilder estimatedCost(final Double estimatedCost) {
            this.estimatedCost = estimatedCost;
            return this;
        }

        public TripBuilder user(final User user) {
            this.user = user;
            return this;
        }

        public Trip build() {
            return new Trip(this.id, this.name, this.destination, this.startDate, this.endDate, this.estimatedCost, this.user);
        }

        public String toString() {
            return "Trip.TripBuilder(id=" + this.id + ", name=" + this.name + ", destination=" + this.destination + ", startDate=" + this.startDate + ", endDate=" + this.endDate + ", estimatedCost=" + this.estimatedCost + ", user=" + this.user + ")";
        }
    }
}


