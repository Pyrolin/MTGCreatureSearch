package com.example.mtgcreaturesearch.test

import io.cucumber.junit.CucumberOptions
import io.cucumber.junit.Cucumber
import org.junit.runner.RunWith
@RunWith(Cucumber::class)
@CucumberOptions(
    plugin = ["pretty", "html:target/cucumber-report.html"],
    features = ["src/main/res/com/example/mtgcreaturesearch/test"])
class RunCucumberTest