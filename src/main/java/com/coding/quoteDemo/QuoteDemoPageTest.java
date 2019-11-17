package com.coding.quoteDemo;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.coding.driver.DriverManager;
import com.coding.helper.BaseHelper;
import com.coding.helper.LaunchWebUrl;
import com.coding.helper.LocatorTypes;

public class QuoteDemoPageTest extends BaseHelper implements IQuotePageInput {
	
	boolean testResult = false;
	LaunchWebUrl launchUrl = new LaunchWebUrl();
	QuoteDemoPageMethods quotePageMethods = new QuoteDemoPageMethods();
	
	public QuoteDemoPageTest() {
		getElementPath("quotePageElements.properties");
		
	}
	
	@BeforeTest
	public void beforeTest() {
		try {
			testResult = launchUrl.loadAppUrl();
		} catch (Exception e) {
			testResult &= false;
			Reporter.log("An error occured during execution : " + e.getMessage());
		}
		
		if (testResult) {
			Reporter.log("Before Test method passed");
			Assert.assertTrue(testResult);
		} else {
			Reporter.log("Before Test method failed");
			Assert.fail();
		}
	}

	@AfterTest
	public void closeDriver() {
		DriverManager.driver.quit();
	}
	
	/**
	 * Enter quote with length less that 10
	 * 
	 */
	//TODO: Currently Test case is failing due to Bug 1
	@Test(priority = 1, alwaysRun = true)
	public void TC01_addQuoteWithInvalidLength() {
		try {
			Reporter.log("Quote length less than 10 should not save the quote verification");
			testResult = quotePageMethods.addNewQuote(AUTHOR + RandomStringUtils.randomAlphanumeric(4),
					QUOTE_SHORT_LENGTH, null);
			Reporter.log("Quote length more than 200 should not save the quote verification");
			testResult &= quotePageMethods.addNewQuote(AUTHOR + RandomStringUtils.randomAlphanumeric(4),
					RandomStringUtils.randomAlphanumeric(210), null);

		} catch (Exception e) {
			testResult &= false;
			Reporter.log("An error occured during execution : " + e.getMessage());
		}
		if (testResult) {
			Reporter.log("<b>TC01_VerifyingLoginDetails method passed");
			Assert.assertTrue(testResult);
		} else {
			Reporter.log("<b>TC01_VerifyingLoginDetails method failed");
			Assert.fail();
		}

	}

	/**
	 * failed due to Bug 2
	 */
	@Test(priority = 2, alwaysRun = true)
	public void TC02_addQuoteWithConsequiteWord() {
		try {
			Reporter.log("Add a quote with all valid details");
			testResult = quotePageMethods.addNewQuote(AUTHOR_ZWEI, VALID_QUOTE, null);
			testResult &= quotePageMethods.validateConsequitiveAuthorQuote(AUTHOR_ZWEI,
					webElemProperty.getProperty("SEARCHED_CONTENT"), LocatorTypes.XPATH, CONSEQUITIVE_QUOTE);
		} catch (Exception e) {
			testResult &= false;
			Reporter.log("An error occured during execution : " + e.getMessage());
		}
		if (testResult) {
			Reporter.log("<b>TC01_VerifyingLoginDetails method passed");
			Assert.assertTrue(testResult);
		} else {
			Reporter.log("<b>TC01_VerifyingLoginDetails method failed");
			Assert.fail();
		}
	}
	
	@Test(priority = 3, alwaysRun = true)
	public void TC03_addQuoteFromOtherLink() {
		try {
			
			testResult = quotePageMethods.addQuoteFromVarioudLink(SECOND_LINK, webElemProperty.getProperty("AUTHOR_GIDEON"), AUTHOR_NAME_GIDEON);
			
		} catch (Exception e) {
			testResult &= false;
			Reporter.log("An error occured during execution : " + e.getMessage());
		}
		if (testResult) {
			Reporter.log("<b>TC01_VerifyingLoginDetails method passed");
			Assert.assertTrue(testResult);
		} else {
			Reporter.log("<b>TC01_VerifyingLoginDetails method failed");
			Assert.fail();
		}
	}
}
