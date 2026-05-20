package com.qa.opencart.factory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
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

		this.prop = prop;

		String browserName = prop.getProperty("browser").trim();

		System.out.println("Browser name is: " + browserName);

		Playwright playwright = Playwright.create();
		tlPlaywright.set(playwright);

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
			throw new RuntimeException("Invalid browser name: " + browserName);
		}

		BrowserContext context = getBrowser().newContext();
		tlBrowserContext.set(context);

		Page page = context.newPage();
		tlPage.set(page);

		page.navigate(prop.getProperty("url"));

		return page;

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
			throw new RuntimeException("config.properties not found");
		} catch (IOException e) {
			throw new RuntimeException("Failed to load config.properties");
		}

		return prop;
	}

	public static String takeScreenshot() {
		Page page = getPage();

		if (page == null) {
			System.out.println("Page is null - skipping screenshot");
			return "";
		}
		try {
			String folder = System.getProperty("user.dir") + "/screenshot";
			Files.createDirectories(Paths.get(folder));

			byte[] screenshot = page.screenshot(new Page.ScreenshotOptions()
					.setPath(Paths.get(folder + "/" + System.currentTimeMillis() + ".png")).setFullPage(true));
			return Base64.getEncoder().encodeToString(screenshot);

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static void cleanup() {
		try {
			if (getPage() != null)
				getPage().close();
			if (getBrowserContext() != null)
				getBrowserContext().close();
			if (getBrowser() != null)
				getBrowser().close();
			if (getPlaywright() != null)
				getPlaywright().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
