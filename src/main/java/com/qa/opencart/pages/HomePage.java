package com.qa.opencart.pages;

import com.microsoft.playwright.Page;

public class HomePage {

	private Page page;

	// 1. String Locators:
	private String search = "input[name='search']";
	private String searchIcon = "button.btn.btn-default.btn-lg";
	private String searchPageHeader = "div#content h1";
	private String loginLink = "//a[normalize-space()='Login']";
	private String myAccountLink = "//span[normalize-space()='My Account']";

	// 2. page constructor:
	public HomePage(Page page) {
		this.page = page;
	}

	// 3. page actions/methods:
	public String getHomePageTitle() {
		String title = page.title();
		System.out.println("Page title is: " + title);

		return title;
	}

	public String getHomePageURL() {
		String url = page.url();
		System.out.println("Page url is: " + url);

		return url;
	}

	public String doSearch(String productName) {
		page.fill(search, productName);
		page.click(searchIcon);
		String header = page.textContent(searchPageHeader);
		System.out.println("Search header is: " + header);

		return header;
	}

	public LoginPage navigateToLoginPage() {
		page.click(myAccountLink);
		page.click(loginLink);

		return new LoginPage(page);
	}

}
