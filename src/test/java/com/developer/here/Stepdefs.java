package com.developer.here;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.developer.here.utility.ExtentUtility;
import com.relevantcodes.extentreports.LogStatus;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Stepdefs {

	WebDriver driver;
	Set<String> linkUrls;
	ExtentUtility extentUtility;

	@Given("^I am on here developer documnetation page$")
	public void i_am_on_here_developer_documnetation_page() throws Exception {
		extentUtility = new ExtentUtility();
		WebDriverManager.chromedriver().setup();
		extentUtility.startTest();
		extentUtility.startTest("Navigating to home page");
		extentUtility.addStep(LogStatus.INFO, "Opened browser");
		driver = new ChromeDriver();
		extentUtility.addStep(LogStatus.INFO, "Maximizing browser");
		driver.manage().window().maximize();
		extentUtility.addStep(LogStatus.INFO, "Navigating to home page");
		driver.get("https://developer.here.com/documentation");
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		extentUtility.addStep(LogStatus.PASS, "Navigated to home page");
		extentUtility.endTest();
	}

	@When("^I get all the links on the page$")
	public void i_get_all_the_links_on_the_page() throws Exception {
		List<WebElement> links = driver.findElements(By.xpath("//a[@href]"));
		linkUrls = new HashSet<String>();
		for (WebElement link : links) {
			String linkUrl = link.getAttribute("href");
			if (linkUrl.contains("developer.here.com/documentation")) {
				linkUrls.add(linkUrl);
			}
		}
		int i = 1;
		for (String link : linkUrls) {
			System.out.println(i++ + " : " + link);
		}
		driver.close();
		driver.quit();
	}

	@Then("^I verify all links for 200 status code$")
	public void i_verify_all_links_for_status_code() throws Exception {
		RequestSpecification request = RestAssured.given();
		extentUtility.startTest("Checking for status code");
		for (String link : linkUrls) {
			try {
				Response response = request.when().get(link).thenReturn();
				int statusCode = response.getStatusCode();
				if (statusCode == 200) {
					extentUtility.addStep(LogStatus.PASS, "Status code is 200 for " + link);
				} else {
					extentUtility.addStep(LogStatus.FAIL, "Status code is not 200 for " + link);
				}
			} catch (Exception e) {
				extentUtility.addStepWithScreenshot(LogStatus.FAIL, "Exception occured while checking status code",
						e.getMessage());
				e.printStackTrace();
			}
		}
		extentUtility.endTest();
	}

	@Then("^I verify angualr is initialized for all pages$")
	public void i_verify_angualr_is_initialized_for_all_pages() throws Exception {
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("--headless");
		ChromeDriver chromeDriver = new ChromeDriver(chromeOptions);
		extentUtility.startTest("Checking if angular is initialized");
		chromeDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		JavascriptExecutor js = chromeDriver;
		for (String link : linkUrls) {
			try {
				chromeDriver.get(link);
				Object message = js.executeScript("return angular.bootstrap");
				extentUtility.addStep(LogStatus.PASS, "Angular loaded on page : " + link);
			} catch (Exception e) {
				if (e.getMessage().contains("angular is not defined")) {
					extentUtility.addStep(LogStatus.FAIL, "Could not find angular on page : " + link);
				} else {
					extentUtility.addStepWithScreenshot(LogStatus.FAIL, "Exception occured while checking status code",
							e.getMessage());
				}
				e.printStackTrace();
			}
		}
		chromeDriver.close();
		chromeDriver.quit();
		extentUtility.endTest();
		extentUtility.endTestReport();
	}
}