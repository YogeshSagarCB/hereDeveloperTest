package com.developer.here.utility;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class ExtentUtility {
	
	ExtentReports extentReports;
	ExtentTest extentTest;
	
	public void startTest() {
		extentReports = new ExtentReports(System.getProperty("user.dir")+"//target//report.html");
	}
	
	public void startTest(String testName) {
		extentTest = extentReports.startTest(testName);
	}
	
	public void endTest() {
		extentReports.endTest(extentTest);
		extentReports.flush();
	}

	public void addStep(LogStatus logStatus, String message) {
		extentTest.log(logStatus, message);
	}
	
	public void addStepWithScreenshot(LogStatus logStatus, String message,String screenshot) {
		extentTest.log(logStatus, message, screenshot);
	}
	
	public void endTestReport() {
		extentReports.flush();
		extentReports.close();
	}
	
}
