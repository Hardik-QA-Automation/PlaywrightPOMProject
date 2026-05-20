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

	protected PlaywrightFactory pf;
	protected Page page;
	protected Properties prop;

	protected HomePage homePage;
	protected LoginPage loginPage;

	@BeforeClass
	@Parameters({ "browser" })
	public void setup(String browserName) {

		pf = new PlaywrightFactory();

		prop = pf.init_prop();

		if (browserName != null && !browserName.isEmpty()) {
			prop.setProperty("browser", browserName);
		}

		page = pf.initBrowser(prop);
		homePage = new HomePage(page);

	}

	@AfterClass(alwaysRun = true)
	public void tearDown() {

		try {
			if (PlaywrightFactory.getPage() != null) {
				PlaywrightFactory.getPage().close();
			}

			if (PlaywrightFactory.getBrowserContext() != null) {
				PlaywrightFactory.getBrowserContext().close();
			}

			if (PlaywrightFactory.getBrowser() != null) {
				PlaywrightFactory.getBrowser().close();
			}

			if (PlaywrightFactory.getPlaywright() != null) {
				PlaywrightFactory.getPlaywright().close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
