package com.supercoolproject.tourista.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.supercoolproject.tourista.cucumber.glue"
)
public class CucumberIntegrationTest {
}