package com.supercoolproject.tourista;

import com.supercoolproject.tourista.controller.*;
import com.supercoolproject.tourista.entity.Event;
import com.supercoolproject.tourista.entity.Trip;
import com.supercoolproject.tourista.entity.User;
import com.supercoolproject.tourista.exception.*;
import com.supercoolproject.tourista.factory.ServiceFactory;
import com.supercoolproject.tourista.factory.ServiceType;
import com.supercoolproject.tourista.repository.EventRepository;
import com.supercoolproject.tourista.service.TripService;
import com.supercoolproject.tourista.service.UserService;
import com.supercoolproject.tourista.service.EventService;

import jakarta.servlet.http.HttpServletRequest;

import com.supercoolproject.tourista.controlleradvice.*;

import org.apache.catalina.core.ApplicationContext;
import org.hibernate.internal.util.compare.CalendarComparator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.io.*;
import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class TouristaApplicationTests {

	private static final Logger logger = LoggerFactory.getLogger(TouristaApplication.class.getName());

	@Test
	void test_generate_trip() {
		logger.info("what");
		Trip parisTrip = Trip.builder()
				.name("Paris Trip")
				.destination("Paris, France")
				.startDate(LocalDate.of(2024, 5, 10))
				.endDate(LocalDate.of(2024, 5, 17))
				.estimatedCost(100000.00)
				.build();

		assertEquals(parisTrip.getName(), "Paris Trip");
		assertEquals(parisTrip.getDestination(), ("Paris, France"));
		assertEquals(parisTrip.getEstimatedCost(), 100000.0);
	}

	// global test reference for tripService dictionary
	@Autowired
	TouristaApplication app = new TouristaApplication();

	@Autowired
	TripService tService = new TripService(app.tripRepository);

	
	@Test
	void test_get_trips() {
		assert(tService.getTrips().size() == 3);
		assert(tService.getTrips().get(0).getName().equals("Paris Trip"));
	}

	@Test
	void test_get_trip() {
		List<Trip> tripsList = tService.getTrips();
		UUID idHold = tripsList.get(0).getId();
		Trip firstTrip = tService.getTrip(idHold).get();

		assert(firstTrip.getName().equals("Paris Trip"));
		assert(firstTrip.getDestination().equals("Paris, France"));
		assert(firstTrip.getEstimatedCost() == 100000.0);
	}

	@Test
	void test_trip_save() {
		Trip berlinTrip = Trip.builder()
			.name("Berlin Trip")
			.destination("Berlin, Germany")
			.startDate(LocalDate.of(2024, 5, 10))
			.endDate(LocalDate.of(2024, 5, 17))
			.estimatedCost(75000.00)
			.build();

		tService.save(berlinTrip);
		List<Trip> tripsList = tService.getTrips();
		int lastIndex = tripsList.size() - 1;
		Trip berlinTripFromList = tripsList.get(lastIndex);
		assert(berlinTripFromList.getName().equals("Berlin Trip"));
		assert(berlinTripFromList.getDestination().equals("Berlin, Germany"));
		assert(berlinTripFromList.getEstimatedCost() == 75000.0);
	}

	@Test
	void test_trip_delete() {
		logger.info("list size = " + tService.getTrips().size());
		int startSize = tService.getTrips().size();
		UUID idHold = tService.getTrips().get(2).getId();
		tService.delete(idHold);
		int endSize = tService.getTrips().size();

		assert(startSize > endSize);
	}

	@Autowired
	UserService uService = new UserService(app.userRepository);

	@Test
	void test_generate_user() {
		assert(uService.userRepositorySize() > 0);
	}

	@Test
	void test_user_save() {
		int startSize = uService.userRepositorySize();
		User tyler = User.builder()
			.username("TWizard")
			.password(app.passwordEncoder.encode("password"))
			.build();

		uService.save(tyler);
		int endSize = uService.userRepositorySize();

		assert(startSize < endSize);
	}

	@Test
	void test_load_user() {
		UserDetails artemisDetails = uService.loadUserByUsername("BeefyRat");
		assert(artemisDetails.getUsername().equals("BeefyRat"));
		assert(artemisDetails.getAuthorities().toString().equals("[ROLE_USER]"));
	}

	@Test
	void test_index() {
		TripController tController = new TripController();
		assert(tController.index().equals("index"));
	}

	@Test
	void test_notifications() {
		TripController tController = new TripController();
		assert(tController.notifications().equals("profiles/notifications"));
	}

	@Test
	void test_signup() {
		TripController tController = new TripController();
		assert(tController.signup().equals("profiles/signup"));
	}

	@Test
	void test_login_controller() {
		LoginController lController = new LoginController();
		assert(lController.login().equals("/profiles/login"));
	}

	@Test
	void test_trip_controller_url() {
		TripController tController = new TripController();
		assertEquals("profiles/notifications", tController.notifications());
		assertEquals("profiles/login", tController.login());
		assertEquals("profiles/signup", tController.signup());
		assertEquals("trip/add", tController.add());
		assertEquals("index", tController.index());

	}

	
	@Test
	void test_global_default_exception_handler() {
		ControllerException exception = new ControllerException(HttpStatus.ACCEPTED, "This is a test");

	}

	@Test
	void test_set_id(){
		Event lunchEvent = Event.builder()
			.name("lunch")
			.location("Domino's")
			.startDateTime(LocalDateTime.of(2024, 6, 17, 1, 30))
			.endDateTime(LocalDateTime.of(2024, 6, 17, 2, 30))
			.estimatedCost(30.00)
			.build();

		List<Trip> tripsList = tService.getTrips();
		UUID idHold = tripsList.get(0).getId();

		lunchEvent.setId(idHold);
		assertEquals(idHold, lunchEvent.getId());
	}

	@Test
	void test_set_name(){
		Event lunchEvent = Event.builder()
			.name("lunch")
			.location("Domino's")
			.startDateTime(LocalDateTime.of(2024, 6, 17, 1, 30))
			.endDateTime(LocalDateTime.of(2024, 6, 17, 2, 30))
			.estimatedCost(30.00)
			.build();

		String nameHold = "Testing string";

		lunchEvent.setName(nameHold);
		assertEquals(nameHold, lunchEvent.getName());

	}

	@Test
	void test_set_location(){
		Event lunchEvent = Event.builder()
			.name("lunch")
			.location("Domino's")
			.startDateTime(LocalDateTime.of(2024, 6, 17, 1, 30))
			.endDateTime(LocalDateTime.of(2024, 6, 17, 2, 30))
			.estimatedCost(30.00)
			.build();

		String nameHold = "Testing string";

		lunchEvent.setLocation(nameHold);
		assertEquals(nameHold, lunchEvent.getLocation());
	}

	@Test
	void test_set_start_date(){
		Event lunchEvent = Event.builder()
			.name("lunch")
			.location("Domino's")
			.startDateTime(LocalDateTime.of(2024, 6, 17, 1, 30))
			.endDateTime(LocalDateTime.of(2024, 6, 17, 2, 30))
			.estimatedCost(30.00)
			.build();

		LocalDateTime dt = LocalDateTime.of(2024, 6, 17, 1, 30);

		lunchEvent.setStartDateTime(dt);
		assertEquals(dt, lunchEvent.getStartDateTime());

	}

	@Test
	void test_set_end_date(){
		Event lunchEvent = Event.builder()
			.name("lunch")
			.location("Domino's")
			.startDateTime(LocalDateTime.of(2024, 6, 17, 1, 30))
			.endDateTime(LocalDateTime.of(2024, 6, 17, 2, 30))
			.estimatedCost(30.00)
			.build();

		LocalDateTime dt = LocalDateTime.of(2024, 6, 17, 1, 30);

		lunchEvent.setEndDateTime(dt);
		assertEquals(dt, lunchEvent.getEndDateTime());
	}

	@Test
	void test_set_estimated_cost(){
		Event lunchEvent = Event.builder()
			.name("lunch")
			.location("Domino's")
			.startDateTime(LocalDateTime.of(2024, 6, 17, 1, 30))
			.endDateTime(LocalDateTime.of(2024, 6, 17, 2, 30))
			.estimatedCost(30.00)
			.build();

		Double estCost = 5000.0;

		lunchEvent.setEstimatedCost(estCost);
		assertEquals(estCost, lunchEvent.getEstimatedCost());
	}

	@Test
	void test_equals(){
		Trip parisTrip = Trip.builder()
				.name("Paris Trip")
				.destination("Paris, France")
				.startDate(LocalDate.of(2024, 5, 10))
				.endDate(LocalDate.of(2024, 5, 17))
				.estimatedCost(100000.00)
				.build();

		Event lunchEvent = Event.builder()
			.name("lunch")
			.id(parisTrip.getId())
			.trip(parisTrip)
			.location("Domino's")
			.startDateTime(LocalDateTime.of(2024, 6, 17, 1, 30))
			.endDateTime(LocalDateTime.of(2024, 6, 17, 2, 30))
			.estimatedCost(30.00)
			.build();

			Event lunchEvent2 = Event.builder()
			.name("lunch")
			.location("Domino's")
			.startDateTime(LocalDateTime.of(2024, 5, 17, 1, 30))
			.endDateTime(LocalDateTime.of(2024, 6, 17, 2, 30))
			.estimatedCost(30.00)
			.build();

		boolean test1 = lunchEvent.equals(lunchEvent);
		boolean test2 = lunchEvent.equals(lunchEvent2);

		assertTrue(test1);
		assertFalse(test2);
	}

	@Test
	void test_tostring(){
		Event lunchEvent = Event.builder()
			.name("lunch")
			.location("Domino's")
			.startDateTime(LocalDateTime.of(2024, 6, 17, 1, 30))
			.endDateTime(LocalDateTime.of(2024, 6, 17, 2, 30))
			.estimatedCost(30.00)
			.build();

		String hold = lunchEvent.toString();
		assertEquals("Event(id=null, name=lunch, location=Domino's, startDateTime=2024-06-17T01:30, endDateTime=2024-06-17T02:30, estimatedCost=30.0)", hold);
	}

	@Test
	void test_get_trip_event(){
		Trip parisTrip = Trip.builder()
				.name("Paris Trip")
				.destination("Paris, France")
				.startDate(LocalDate.of(2024, 5, 10))
				.endDate(LocalDate.of(2024, 5, 17))
				.estimatedCost(100000.00)
				.build();

		Event lunchEvent = Event.builder()
			.name("lunch")
			.id(parisTrip.getId())
			.trip(parisTrip)
			.location("Domino's")
			.startDateTime(LocalDateTime.of(2024, 6, 17, 1, 30))
			.endDateTime(LocalDateTime.of(2024, 6, 17, 2, 30))
			.estimatedCost(30.00)
			.build();

		Trip tHold = lunchEvent.getTrip();
		assertEquals(parisTrip, tHold);
	}

	@Test
	void test_set_id_trip(){
		Trip parisTrip = Trip.builder()
				.name("Paris Trip")
				.destination("Paris, France")
				.startDate(LocalDate.of(2024, 5, 10))
				.endDate(LocalDate.of(2024, 5, 17))
				.estimatedCost(100000.00)
				.build();

		List<Trip> tripsList = tService.getTrips();
		UUID idHold = tripsList.get(0).getId();

		parisTrip.setId(idHold);
		assertEquals(idHold, parisTrip.getId());
	}

	@Test
	void test_set_name_trip(){
		Trip parisTrip = Trip.builder()
				.name("Paris Trip")
				.destination("Paris, France")
				.startDate(LocalDate.of(2024, 5, 10))
				.endDate(LocalDate.of(2024, 5, 17))
				.estimatedCost(100000.00)
				.build();

		String nameHold = "Testing string";

		parisTrip.setName(nameHold);
		assertEquals(nameHold, parisTrip.getName());
	}

	@Test
	void test_set_destination_trip(){
		Trip parisTrip = Trip.builder()
				.name("Paris Trip")
				.destination("Paris, France")
				.startDate(LocalDate.of(2024, 5, 10))
				.endDate(LocalDate.of(2024, 5, 17))
				.estimatedCost(100000.00)
				.build();

		String nameHold = "Testing string";

		parisTrip.setDestination(nameHold);
		assertEquals(nameHold, parisTrip.getDestination());
	}

	@Test
	void test_set_start_date_trip(){
		Trip parisTrip = Trip.builder()
				.name("Paris Trip")
				.destination("Paris, France")
				.startDate(LocalDate.of(2024, 5, 10))
				.endDate(LocalDate.of(2024, 5, 17))
				.estimatedCost(100000.00)
				.build();

		LocalDate date = LocalDate.of(2025, 5, 17);

		parisTrip.setStartDate(date);
		assertEquals(date, parisTrip.getStartDate());
	}

	@Test
	void test_set_end_date_trip(){
		Trip parisTrip = Trip.builder()
				.name("Paris Trip")
				.destination("Paris, France")
				.startDate(LocalDate.of(2024, 5, 10))
				.endDate(LocalDate.of(2024, 5, 17))
				.estimatedCost(100000.00)
				.build();

		LocalDate date = LocalDate.of(2025, 5, 17);

		parisTrip.setEndDate(date);
		assertEquals(date, parisTrip.getEndDate());
	}

	@Test
	void test_set_estimated_cost_trip(){
		Trip parisTrip = Trip.builder()
				.name("Paris Trip")
				.destination("Paris, France")
				.startDate(LocalDate.of(2024, 5, 10))
				.endDate(LocalDate.of(2024, 5, 17))
				.estimatedCost(100000.00)
				.build();

		double estCost = 10.0;

		parisTrip.setEstimatedCost(estCost);
		assertEquals(estCost, parisTrip.getEstimatedCost());
	}

	@Test
	void test_equals_trip(){
		Trip parisTrip1 = Trip.builder()
			.name("Paris Trip")
			.destination("Paris, France")
			.startDate(LocalDate.of(2024, 5, 10))
			.endDate(LocalDate.of(2024, 5, 17))
			.estimatedCost(100000.00)
			.build();

		Trip parisTrip2 = Trip.builder()
			.name("Paris Trip")
			.destination("Paris, France")
			.startDate(LocalDate.of(2024, 5, 10))
			.endDate(LocalDate.of(2022, 5, 17))
			.estimatedCost(100000.00)
			.build();

		assertFalse(parisTrip1.equals(parisTrip2));
		assertTrue(parisTrip1.equals(parisTrip1));
	}

	@Test
	void test_set_id_user(){
		User tyler = User.builder()
			.username("TWizard")
			.password(app.passwordEncoder.encode("password"))
			.build();

		List<Trip> tripsList = tService.getTrips();
		UUID idHold = tripsList.get(0).getId();

		tyler.setId(idHold);
		assertEquals(idHold, tyler.getId());
	}

	@Test
	void test_set_username_user(){
		List<Trip> tripsList = tService.getTrips();
		UUID idHold = tripsList.get(0).getId();

		User tyler = User.builder()
			.username("TWizard")
			.password(app.passwordEncoder.encode("password"))
			.id(idHold)
			.build();

		String username = "Testing";

		tyler.setUsername(username);
		assertEquals(username, tyler.getUsername());
	}

	@Test
	void test_set_password_user(){
		User tyler = User.builder()
			.username("TWizard")
			.password(app.passwordEncoder.encode("password"))
			.build();

		String oldPassword = app.passwordEncoder.encode("password");
		String newPassword = app.passwordEncoder.encode("password2");


		tyler.setPassword(newPassword);
		assertNotEquals(oldPassword, tyler.getPassword());
	}

	@Test
	void test_equals_user(){
		User tyler1 = User.builder()
			.username("TWizard")
			.password(app.passwordEncoder.encode("password"))
			.build();
		User tyler2 = User.builder()
			.username("TWizardasdf")
			.password(app.passwordEncoder.encode("password"))
			.build();

		assertFalse(tyler1.equals(tyler2));
		assertTrue(tyler1.equals(tyler1));
	}

	@Test
	void test_hash_user(){
		User tyler = User.builder()
			.username("TWizard")
			.password(app.passwordEncoder.encode("password"))
			.build();

		int hash = tyler.hashCode();

		assertEquals(hash, tyler.hashCode());
	}

	@Test
	void test_get_trips_user(){
		User tyler = User.builder()
			.username("TWizard")
			.password(app.passwordEncoder.encode("password"))
			.build();

		assertEquals(0, tyler.getTrips().size());
	}

	@Test
	void test_controller_exception_httpstatus() {
		ControllerException exception = new ControllerException(HttpStatus.ACCEPTED, "testing message");
		assertEquals(HttpStatus.ACCEPTED, exception.getHttpStatus());
		assertEquals("202:testing message", exception.errorMessage());
	}

	@Test
	void test_service_factory_init() {
		ServiceFactory fac = new ServiceFactory();
	}

	@Test
	void test_service_type() {
		assertEquals("EventService", ServiceType.EVENT_SERVICE.getServiceName());
		assertEquals("TripService", ServiceType.TRIP_SERVICE.getServiceName());
		assertEquals("UserService", ServiceType.USER_SERVICE.getServiceName());
	}

	@Test
	void test_profile() {
		UserController uController = new UserController(app.passwordEncoder);
		assertEquals("profiles/profile", uController.profile());
	}

	
}
