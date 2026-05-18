package com.qa.opencart.base;

import java.util.Properties;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import com.microsoft.playwright.Page;
import com.qa.opencart.factory.PlaywrightFactory;
import com.qa.opencart.pages.HomePage;
import com.qa.opencart.pages.LoginPage;

public class BaseTest {

	PlaywrightFactory pf;
	Page page;
	protected Properties prop;

	protected HomePage homePage;
	protected LoginPage loginPage;

	@BeforeClass
	@Parameters({ "browser" })
	public void setup(String browserName) {

		pf = new PlaywrightFactory();

		prop = pf.init_prop();

		if (browserName != null) {
			prop.setProperty("browser", browserName);
		}

		page = pf.initBrowser(prop);
		homePage = new HomePage(PlaywrightFactory.getPage());

	}

	@AfterClass
	public void tearDown() {
		PlaywrightFactory.getPage().close();
		PlaywrightFactory.getBrowser().close();
		PlaywrightFactory.getPlaywright().close();
	}
}
