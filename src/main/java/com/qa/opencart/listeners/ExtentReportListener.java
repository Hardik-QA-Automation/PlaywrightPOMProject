package com.qa.opencart.listeners;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import static com.qa.opencart.factory.PlaywrightFactory.takeScreenshot;

public class ExtentReportListener implements ITestListener {

	private static final String OUTPUT_FOLDER = System.getProperty("user.dir") + "/build/";

	private static final String FILE_NAME = "TestExecutionReport.html";

	private static ExtentReports extent = init();
	public static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

	private static ExtentReports init() {

		try {
			Files.createDirectories(Paths.get(OUTPUT_FOLDER));
		} catch (IOException e) {
			e.printStackTrace();
		}

		ExtentSparkReporter reporter = new ExtentSparkReporter(OUTPUT_FOLDER + FILE_NAME);

		reporter.config().setReportName("Open Cart Automation Test Results");

		ExtentReports extentReports = new ExtentReports();
		extentReports.attachReporter(reporter);

		return extentReports;
	}

	@Override
	public void onStart(ITestContext context) {
		System.out.println("Test Suite started!");
	}

	@Override
	public void onFinish(ITestContext context) {
		System.out.println("Test Suite finished!");

		extent.flush();
		test.remove();
	}

	@Override
	public void onTestStart(ITestResult result) {

		ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());

		test.set(extentTest);
		test.get().getModel().setStartTime(getTime(result.getStartMillis()));
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		test.get().pass("Test passed");
		test.get().getModel().setEndTime(getTime(result.getEndMillis()));
	}

	@Override
	public void onTestFailure(ITestResult result) {

		test.get().fail(result.getThrowable());

		String screenshot = takeScreenshot();

		if (!screenshot.isEmpty()) {
			test.get().fail(MediaEntityBuilder.createScreenCaptureFromBase64String(screenshot).build());
		}

		test.get().getModel().setEndTime(getTime(result.getEndMillis()));
	}

	@Override
	public void onTestSkipped(ITestResult result) {

		test.get().skip(result.getThrowable());

		String screenshot = takeScreenshot();

		if (!screenshot.isEmpty()) {
			test.get().skip(MediaEntityBuilder.createScreenCaptureFromBase64String(screenshot).build());
		}

		test.get().getModel().setEndTime(getTime(result.getEndMillis()));
	}

	private Date getTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}
}