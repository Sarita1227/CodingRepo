package com.coding.helper;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.Reporter;

import com.coding.driver.DriverManager;
import com.coding.driver.DriverManagerFactory;
import com.coding.driver.DriverType;

public class LaunchWebUrl extends BaseHelper{
	
	DriverManager webDr;
			
	@SuppressWarnings("static-access")
	public boolean loadAppUrl() throws Exception {
		boolean returnValue = false;
		webDr = DriverManagerFactory.getManager(DriverType.CHROME);
		DriverManager.driver = webDr.getDriver();
		Reporter.log("Launching Page URL");
		DriverManager.driver.navigate().to("https://www-5daeea75265983482b3f513c.recruit.eb7.io/");
		waitForPageLoad();
		Reporter.log("Verify the Url is loaded successfully");
		WebElement loginPageELement = findElement(webElemProperty.getProperty("ADD_QUOTE_BTN"), LocatorTypes.XPATH);
		Reporter.log("Verify page title is displayed");
		returnValue = loginPageELement.isDisplayed();
		returnValue &= loginPageELement.getText().trim().equalsIgnoreCase("Add new quote");

		if (returnValue) {
			Reporter.log("Successfully loaded the Page URL");
			Assert.assertTrue(returnValue);
		} else {
			Reporter.log("URL loading method failedL");
			Assert.fail();
		}
		return returnValue;

	}

}
