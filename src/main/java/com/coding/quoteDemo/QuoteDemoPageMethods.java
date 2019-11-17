package com.coding.quoteDemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.Reporter;

import com.coding.driver.DriverManager;
import com.coding.helper.BaseHelper;
import com.coding.helper.LocatorTypes;

public class QuoteDemoPageMethods extends BaseHelper {
	
	boolean returnValue = false;
	
	/**
	 * Add new quote
	 * @param authorName
	 * @param quoteName
	 * @return
	 * @throws InterruptedException
	 */
	public boolean addNewQuote(String authorName, String quoteName, List<String> previousQuote) throws InterruptedException {
		Reporter.log("Verify add new quote is displayed in the page");
		WebElement addNewQuote = findElement(webElemProperty.getProperty("ADD_QUOTE_BTN"), LocatorTypes.XPATH);
		returnValue = addNewQuote.isDisplayed();
		if (returnValue) {
			Reporter.log("Click on add new quote button and verify add new quote pop up is displayed");
			addNewQuote.click();
			Thread.sleep(2000);
			WebElement popup = findElement(webElemProperty.getProperty("NEW_QUOTE_POP_UP"), LocatorTypes.XPATH);
			returnValue = popup.isDisplayed();
			WebElement author = findElement(webElemProperty.getProperty("POPUP_AUTHOR_FIELD"), LocatorTypes.XPATH);
			WebElement quote = findElement(webElemProperty.getProperty("POPUP_QUOTE_FIELD"), LocatorTypes.XPATH);
			Reporter.log("Enter Author name");
			author.sendKeys(authorName);
			Reporter.log("Enter Quote");
			quote.sendKeys(quoteName);
			Reporter.log("Click on Save button");
			WebElement saveBtn = findElement(webElemProperty.getProperty("POP_UP_SAVE"), LocatorTypes.XPATH);
			returnValue &= saveBtn.isDisplayed();
			saveBtn.click();
			Thread.sleep(2000);
			Reporter.log("Verify the quote is successfully saved only if the quote length is between 10 to 200");
			Reporter.log("Length of the current quote is" + quoteName.length());
			WebElement cancel = null;
			returnValue &= searchQuote(authorName, webElemProperty.getProperty("SEARCHED_CONTENT"), LocatorTypes.XPATH);
			if (quoteName.length() < 10 || quoteName.length() > 200) {
				//Below isdisplayed method is not working as it is not specified in the DOM
				//returnValue &= addNewQuote.isDisplayed();			
				
				if (!returnValue) {
					Reporter.log("Unable to save quote , quote length must be between 10 to 200");
					// TODO: error pop up verification must come here
					cancel = findElement(webElemProperty.getProperty("POPUP_CANCEL_BTN"), LocatorTypes.XPATH);
					// Closing the add quote pop up so that it wont affect other test case execution
					cancel.click();
					returnValue &= !addNewQuote.isDisplayed();
				} else {
					Reporter.log("Quote saved successfully inspite of the length issue ");
					returnValue &= false;
				}

			} else {
				Reporter.log("Quote saved successfully ,when the quote is meeting the length criteria");
				returnValue &= true;
			}
			if (previousQuote != null) {

				Reporter.log(
						"Verify Quote is not saved when the current quote is having 3 consequitive word as that of previous quote for same author");
				for (int j = 0; j < previousQuote.size(); j++) {
					String currentWord = previousQuote.get(j);
					String[] splitWord = currentWord.split("\\s*(\\s|=>|,)\\s*");
					ArrayList<String> consequiteWordList = new ArrayList<String>();
					for (int k = 0; k < splitWord.length; k++) {
						if(k != splitWord.length - 2) {
							String newWord = splitWord[k] + " " + splitWord[k + 1] + " " + splitWord[k + 2];
							consequiteWordList.add(newWord);
						} else {
							break;
						}

					}
					for (int i = 0; i < consequiteWordList.size(); i++) {
						if (quoteName.contains(consequiteWordList.get(i))) {
							Reporter.log(
									"Unable to save quote , as the new quote contains 3 consequitive word from the previous quote");
							// TODO: error pop up verification must come here
							returnValue &= searchQuote(authorName, webElemProperty.getProperty("SEARCHED_CONTENT"), LocatorTypes.XPATH);
							if (returnValue) {
								Reporter.log(
										"Quote saved successfully inspite of having three consequitive word from previous quote");
								returnValue &= false;
								break;
							}

						}

					}

				}
			}

		}
		if (returnValue) {
			Reporter.log("<b>addNewQuote method passed");
		} else {
			Reporter.log("<b>addNewQuote method failed");
		}
		return returnValue;
	}

	/**
	 * Add multiple new quote
	 * @param authorQuoteList
	 * @return
	 * @throws InterruptedException
	 */
	public boolean addMultipleQuote(Map<Object, Object> authorQuoteList, String authorName) throws InterruptedException {

		for (int i = 0; i < authorQuoteList.size(); i++) {
			Reporter.log("Adding Quote number " + i);
			returnValue = addNewQuote(authorName, (String) authorQuoteList.get(i), null);
			if (returnValue) {
				Reporter.log("Successfully added quote number" + i);
			} else {
				Reporter.log("Unable to add quote " + i);
			}
		}

		return returnValue;
	}

	/**
	 * Search quote
	 * @param searchName
	 * @param element
	 * @param type
	 * @return
	 */
	public boolean searchQuote(String searchName, String element, LocatorTypes type) {
		Reporter.log("Verify the search bar is enabled");
		WebElement searchBar = findElement(webElemProperty.getProperty("SEARCH_BAR"), LocatorTypes.XPATH);
		returnValue = searchBar.isEnabled();
		if(returnValue) {
			searchBar.clear();
			searchBar.sendKeys(searchName);
			Reporter.log("Wait untill the search item appears");
			absenceOfElementLocated(webElemProperty.getProperty("REFRESH_ICON"), LocatorTypes.XPATH);
			
			WebElement content = findElement(element, type);
			returnValue = content.isDisplayed();
		}
		return returnValue;
	}

	/**
	 * Author and quote section verification
	 * @param authorName
	 * @return
	 * @throws InterruptedException
i	 */
	public List<String> verifyAuthorAndQuoteSection(String authorName) throws InterruptedException {
		List<String> quoteList = new ArrayList<String>();
		Reporter.log("Verify author name are displayed in left side of the search item section");
		WebElement author = findElement(webElemProperty.getProperty("SEARCHED_AUTHOR_SECTION"),
				LocatorTypes.XPATH);
		returnValue = !author.getText().trim().equals(authorName);
		if (returnValue) {
			WebElement quoteTitle = null;
			Reporter.log("Verify quotes are grouped by author name");
			author.click();
			Thread.sleep(2000);
			Reporter.log("Verify the author name is selected or highlighted on selecting");
			returnValue = author.getAttribute("class").contains("active-tab");
			int i = 0;
			List<WebElement> quoteSection = findElements(webElemProperty.getProperty("SEARCHED_QUOTE_SECTION"),
					LocatorTypes.XPATH);
			for (WebElement elem : quoteSection) {
				if (i == 0) {
					Reporter.log("Verify the quote section contains the header name");
					returnValue &= elem.getAttribute("class").contains("quotes__header");
					returnValue &= elem.getText().contains(authorName);
				} else {
					Reporter.log("Verify the quote section contains only one header section");
					returnValue &= !elem.getAttribute("class").contains("quotes__header");
					Reporter.log("Verify the quote section is in display mode, without author name");
					quoteTitle = elem.findElement(By.cssSelector("p.quotes__title"));
					returnValue &= quoteTitle.isDisplayed();
					returnValue &= !quoteTitle.getText().contains(authorName);
					quoteList.add(quoteTitle.getText());
				}
				if (i == 1) {
					Reporter.log("Verify clicking on the quote enable the edit mode");
					elem.click();
					WebElement authorEdit = elem.findElement(By.cssSelector("form>input[name='author']"));
					
					returnValue &= authorEdit.getAttribute("value").contains(authorName);
					WebElement quoteText = elem.findElement(By.cssSelector("form>input[name='text']"));
					returnValue &= quoteText.isDisplayed();
					Reporter.log("Click on Save");
					WebElement saveBtn = elem.findElement(By.cssSelector("form>button[type='submit']"));
					returnValue &= saveBtn.isEnabled();
					click(saveBtn);
					Thread.sleep(2000);
					quoteTitle = elem.findElement(By.cssSelector("p.quotes__title"));
					returnValue &= quoteTitle.isDisplayed();
				}
				i++;
				
			}
		}

		return quoteList;
	}

	public boolean validateConsequitiveAuthorQuote(String authorName, String element, LocatorTypes type, String newQuote) throws InterruptedException {
		returnValue = searchQuote(authorName, element, type);
		List<String> quoteList = verifyAuthorAndQuoteSection(authorName);
		returnValue &= addNewQuote(authorName, newQuote, quoteList);
		return returnValue;
	}

	/**
	 * Add quote from other link
	 * @param URL
	 * @param element
	 * @param name
	 * @return
	 * @throws InterruptedException
	 */
	@SuppressWarnings("unchecked")
	public boolean addQuoteFromVarioudLink(String URL, String element, String name) throws InterruptedException {
		Reporter.log("Open other link in new tab");
		((JavascriptExecutor) DriverManager.driver).executeScript("window.open()");
		ArrayList<String> tabs = new ArrayList<String>(DriverManager.driver.getWindowHandles());
		DriverManager.driver.switchTo().window(tabs.get(1));
		DriverManager.driver.navigate().to(URL);

		//Thread.sleep(3000);
		Map map = new HashMap();
		List<WebElement> authorElement = findElements(element, LocatorTypes.XPATH);
		returnValue = !authorElement.isEmpty();
		for(int i=0; i <authorElement.size(); i++) {
			
			WebElement quoteEle = findElement("(//p/strong[text()='Gideon'])[" + (i+1) + "]/parent::p", LocatorTypes.XPATH);
			String quote = (quoteEle.getText()).replace("Gideon: ", "\"").trim();
			
			map.put(i, quote);
		}
		Reporter.log("Switch back to the main tab after fetching all the required quote");
		DriverManager.driver.switchTo().window(tabs.get(0));
		Thread.sleep(3000);
		Reporter.log("Add Quote to the database");
		returnValue &= addMultipleQuote(map, name);
		
		return returnValue;
	}
}
