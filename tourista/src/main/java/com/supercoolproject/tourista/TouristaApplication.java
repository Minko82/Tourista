package com.supercoolproject.tourista;

import com.supercoolproject.tourista.entity.Event;
import com.supercoolproject.tourista.entity.Trip;
import com.supercoolproject.tourista.entity.User;
import com.supercoolproject.tourista.repository.EventRepository;
import com.supercoolproject.tourista.repository.TripRepository;
import com.supercoolproject.tourista.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class TouristaApplication {

//	class Logger {
//    public void log(String message) {
//        System.out.println("Logging: " + message);
//    }
//}
	// Use singleton---if statement to see if the instance of object is null.
	// If null, create a new instance. If not null, return the instance.
	// The following
//class UserManager {
//    private Logger logger;
//
//    public UserManager(Logger logger) {
//        this.logger = logger;
//    }
//
//    public void createUser(String username) {
//        logger.log("Creating user: " + username);
//    }
//}
//
//public class Main {
//    public static void main(String[] args) {
//        Logger logger = new Logger();
//        UserManager userManager = new UserManager(logger);
//        userManager.createUser("John Doe");
//    }
//}
	public static ApplicationContext applicationContext;

	public static void main(String[] args) {
		applicationContext = SpringApplication.run(TouristaApplication.class, args);
		System.err.println(applicationContext.toString());
	}

	@Autowired
	TripRepository tripRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;


	// Runs this method when the application starts, which creates temporary fake data https://medium.com/@truongbui95/spring-boots-application-events-36ebe09e9313
	@EventListener(ApplicationReadyEvent.class)
	public void generateUsers() {
		Trip parisTrip = Trip.builder()
				.name("Paris Trip")
				.destination("Paris, France")
				.startDate(LocalDate.of(2024, 5, 10))
				.endDate(LocalDate.of(2024, 5, 17))
				.estimatedCost(100000.00)
				.build();

		Event lunchEvent = Event.builder()
				.name("lunch")
				.location("Domino's")
				.startDateTime(LocalDateTime.of(2024, 6, 17, 1, 30))
				.endDateTime(LocalDateTime.of(2024, 6, 17, 2, 30))
				.estimatedCost(30.00)
				.build();

		Event sightseeingEvent = Event.builder()
				.name("Eiffel Tower")
				.location("Paris")
				.startDateTime(LocalDateTime.of(2024, 6, 19, 11, 0))
				.endDateTime(LocalDateTime.of(2024, 6, 19, 1, 0))
				.estimatedCost(0.00)
				.build();

		Trip egyptTrip = Trip.builder()
				.name("Egypt Trip")
				.destination("Cairo, Egypt")
				.startDate(LocalDate.of(2024, 6, 10))
				.endDate(LocalDate.of(2024, 6, 17))
				.estimatedCost(54328.00)
				.build();

		Trip romeTrip = Trip.builder()
				.name("Rome Trip")
				.destination("Rome, Italy")
				.startDate(LocalDate.of(2025, 10, 2))
				.endDate(LocalDate.of(2025, 12, 17))
				.estimatedCost(39328.00)
				.build();

		User beefyRat = User.builder()
				.username("BeefyRat")
				.password(passwordEncoder.encode("testing"))
				.build();

		User angela = User.builder()
				.username("Angela")
				.password(passwordEncoder.encode("Idk"))
				.build();

		User bob = User.builder()
				.username("Bob")
				.password(passwordEncoder.encode("Testing"))
				.build();


		parisTrip.addEvent(lunchEvent);
		parisTrip.addEvent(sightseeingEvent);

		beefyRat.addTrip(parisTrip);
		angela.addTrip(egyptTrip);
		bob.addTrip(romeTrip);


		userRepository.save(beefyRat);
		userRepository.save(angela);
		userRepository.save(bob);
	}
}
