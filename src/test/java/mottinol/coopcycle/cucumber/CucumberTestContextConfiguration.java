package mottinol.coopcycle.cucumber;

import io.cucumber.spring.CucumberContextConfiguration;
import mottinol.coopcycle.IntegrationTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@IntegrationTest
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
