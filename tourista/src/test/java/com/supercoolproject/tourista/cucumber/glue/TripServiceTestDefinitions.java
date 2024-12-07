package com.supercoolproject.tourista.cucumber.glue;

import com.supercoolproject.tourista.entity.Trip;
import com.supercoolproject.tourista.service.TripService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@CucumberContextConfiguration
public class TripServiceTestDefinitions {
    @Autowired
    private TripService tripService;

    private Trip savedTrip;

    @Given("a trip with name {string} and location {string}")
    public void aTripExists(String name, String location) {
        savedTrip = Trip.builder()
                .name(name)
                .destination(location)
                .startDate(LocalDate.of(2024, 5, 10))
                .endDate(LocalDate.of(2024, 5, 17))
                .estimatedCost(100000.00)
                .build();
    }

    @When("the trip is saved")
    public void saveTrip() {
        savedTrip = tripService.save(savedTrip);
    }

    @Then("the trip should be successfully saved in the database")
    public void tripSavedSuccessfully() {
        assertNotNull(savedTrip.getId());
    }
}
