package me.pierrontmaxi.memoire.bscl;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"me.pierrontmaxi.memoire.bscl"},
        plugin = {"pretty"}
)
public class CucumberTestRunner {
}
