package com.lambdatest;
import com.google.gson.JsonObject;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.testng.Assert;

public class PlaywrightTestSingle3 {
    public static void main(String[] args) throws UnsupportedEncodingException {
        try (Playwright playwright = Playwright.create()) {
            JsonObject capabilities = new JsonObject();
            JsonObject ltOptions = new JsonObject();

            String user = "abhidk10";
            String accessKey = "LT_nEmlCCP9cnUijqBObfR4jYluHWxpmm4jBpg0fhHumxCcEYq";
            capabilities.addProperty("browsername", "Chrome"); // Browsers allowed: `Chrome`, `MicrosoftEdge`, `pw-chromium`, `pw-firefox` and `pw-webkit`
            capabilities.addProperty("browserVersion", "latest");
            capabilities.addProperty("video", true);
            ltOptions.addProperty("platform", "Windows 10");
            ltOptions.addProperty("name", "Playwright Test");
            ltOptions.addProperty("build", "Playwright Java Build");
            ltOptions.addProperty("user", user);
            ltOptions.addProperty("accessKey", accessKey);
            capabilities.add("LT:Options", ltOptions);

            BrowserType chromium = playwright.chromium();
            String caps = URLEncoder.encode(capabilities.toString(), "utf-8");
            String cdpUrl = "wss://cdp.lambdatest.com/playwright?capabilities=" + capabilities;
            Browser browser = chromium.connect(cdpUrl);
            Page page = browser.newPage();
            try   {
                
             // Open the LambdaTest Selenium Playground
                page.navigate("https://www.lambdatest.com/selenium-playground");
                
                // Click on "Input Form Submit"
                page.locator("text=Input Form Submit").click();
                
                // Click "Submit" without filling in any information
                page.locator("button.selenium_btn:text('Submit')").click();
      
                Locator nameField = page.locator("input[placeholder='Name']");

                // Assert error message "Please fill out this field."
                String validationMessage = (String) nameField.evaluate("el => el.validationMessage");
                Assert.assertEquals(validationMessage, "Please fill out this field.");
                
                System.out.println("Validation Message:" + validationMessage);
                System.out.println("Test Passed: Mandatory field validation works!");

                // Fill in Name, Email, and other fields
                page.locator("//input[@id='name']").fill("John Doe");
                page.locator("//input[@id='inputEmail4']").fill("john.doe@example.com");
                Locator passwordInput = page.locator("#inputPassword4");
                passwordInput.fill("mySecurePassword123");
                Locator companyInput = page.locator("//input[@placeholder='Company']");
                companyInput.fill("OpenAI");
                page.locator("#websitename").fill("abc@gmail.com");
                Locator countryDropdown = page.locator("select[name='country']");
                countryDropdown.selectOption(new SelectOption().setLabel("India"));
                Locator cityInput = page.locator("//input[@placeholder='City']");
                cityInput.fill("Pune");
                Locator addressInput = page.locator("#inputAddress1");
                addressInput.fill("123 Main Street, Apt 4B");
                Locator addressInput1 = page.locator("#inputAddress2");
                addressInput1.fill("Baner");
                Locator stateInput = page.locator("#inputState");
                stateInput.fill("Maharashtra");
                Locator zipInput = page.locator("#inputZip");
                zipInput.fill("445001");
                
                Locator submitButton = page.locator("button.selenium_btn");

             // Click the submit button
             submitButton.click();
             page.waitForTimeout(3000);
             
             Locator successMessage = page.locator(".success-msg");  
             if (successMessage.isVisible()) {  
                 System.out.println(successMessage.textContent());  
             } else {  
                 System.out.println("Success message is not visible yet.");  
             }  

             	String displayedMessage= successMessage.textContent();

                String message="Thanks for contacting us, we will get back to you shortly.";
                // Validate success message
                
                
                //String displayedMessage = page1.locator("text=Thanks for contacting us, we will get back to you shortly").textContent();
                

				if (displayedMessage.equals(message))
				{
                	setTestStatus("passed", "Message matched", page);
                	System.out.println("✅ Test Passed: The displayed message matches the expected message.");
				} else 
				{
					setTestStatus("failed", "Message not matched", page);
					System.out.println("❌ Test Failed: Expected message '" + message + "', but got '" + displayedMessage + "'.");
				}	
			}				
			catch (Exception err) {
					setTestStatus("failed", "Message not matched", page);
					err.printStackTrace();
            }
            browser.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

	public static void setTestStatus(String status, String remark, Page page) {
		Object result;
		result = page.evaluate("_ => {}",
				"lambdatest_action: { \"action\": \"setTestStatus\", \"arguments\": { \"status\": \"" + status
						+ "\", \"remark\": \"" + remark + "\"}}");
	}
}
