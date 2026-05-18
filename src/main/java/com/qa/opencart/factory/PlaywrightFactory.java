package com.qa.opencart.factory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Properties;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class PlaywrightFactory {

	private static ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();
	private static ThreadLocal<Browser> tlBrowser = new ThreadLocal<>();
	private static ThreadLocal<BrowserContext> tlBrowserContext = new ThreadLocal<>();
	private static ThreadLocal<Page> tlPage = new ThreadLocal<>();

	Properties prop;

	public static Playwright getPlaywright() {
		return tlPlaywright.get();
	}

	public static Browser getBrowser() {
		return tlBrowser.get();
	}

	public static BrowserContext getBrowserContext() {
		return tlBrowserContext.get();
	}

	public static Page getPage() {
		return tlPage.get();
	}

	public Page initBrowser(Properties prop) {

		String browserName = prop.getProperty("browser").trim();

		System.out.println("Browser name is: " + browserName);

		tlPlaywright.set(Playwright.create());
		Playwright playwright = tlPlaywright.get();

		switch (browserName.toLowerCase()) {
		case "chromium":
//			browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
			tlBrowser.set(playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false)));
			break;
		case "firefox":
//			browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
			tlBrowser.set(playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false)));
			break;
		case "safari":
//			browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
			tlBrowser.set(playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false)));
			break;
		case "chrome":
//			browser = playwright.chromium().launch(new LaunchOptions().setChannel("chrome").setHeadless(false));
			tlBrowser.set(playwright.chromium().launch(new LaunchOptions().setChannel("chrome").setHeadless(false)));
			break;
		case "edge":
//			browser = playwright.chromium().launch(new LaunchOptions().setChannel("chrome").setHeadless(false));
			tlBrowser.set(playwright.chromium().launch(new LaunchOptions().setChannel("msedge").setHeadless(false)));
			break;

		default:
//			System.out.println("Please pass the wright browser name.....");
			break;
		}

		tlBrowserContext.set(getBrowser().newContext());
		tlPage.set(getBrowserContext().newPage());
		getPage().navigate(prop.getProperty("url"));
		return getPage();

	}

	/**
	 * this method is used to initialize the properties from config file
	 * 
	 * @return
	 */
	public Properties init_prop() {
		try {
			FileInputStream ip = new FileInputStream("./src/test/resources/config/config.properties");
			prop = new Properties();
			prop.load(ip);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return prop;
	}

	public static String takeScreenshot() {
		String path = System.getProperty("user.dir") + "/screenshot/" + System.currentTimeMillis() + ".png";

		try {
			byte[] screenshot = getPage()
					.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path)).setFullPage(true));

			return Base64.getEncoder().encodeToString(screenshot);

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

}
