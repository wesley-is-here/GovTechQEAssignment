package com.runnertest;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@CucumberOptions(
        features = "Features/Assignment.feature",   // Features
        tags = "@UserStory3",
        glue = "com/steps",
        plugin = {"pretty","html:target/cucumber-reports/WikiPage/cucumber-pretty.html", "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"}
)

public class SequentialTest {

}
