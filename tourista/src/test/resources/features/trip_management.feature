Feature: Trip Management

  Scenario: Saving a trip
    Given a trip with name "Spring Break" and location "Beach"
    When the trip is saved
    Then the trip should be successfully saved in the database