package com.lambdatest;

import com.google.gson.JsonObject;
import com.microsoft.playwright.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class PlaywrightTestSingle {
	public static void main(String[] args) throws UnsupportedEncodingException {
        try (Playwright playwright = Playwright.create()) {
            JsonObject capabilities = new JsonObject();
            JsonObject ltOptions = new JsonObject();

            String user = "abhidk10";
            String accessKey = "LT_nEmlCCP9cnUijqBObfR4jYluHWxpmm4jBpg0fhHumxCcEYq";

            capabilities.addProperty("browsername", "Chrome"); // Browsers allowed: `Chrome`, `MicrosoftEdge`, `pw-chromium`, `pw-firefox` and `pw-webkit`
            capabilities.addProperty("browserVersion", "latest");
            ltOptions.addProperty("platform", "Windows 10");
            capabilities.addProperty("video", true);
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
            try{
		
             

                page.navigate("https://www.lambdatest.com/selenium-playground");
                // Click on "Simple Form Demo"
                page.locator("text=Simple Form Demo").click();
                
                System.out.println("1st");

                // Validate the URL contains "simple-form-demo"
                assert page.url().contains("simple-form-demo");
                
                System.out.println("2st");
                // Create a variable for the string value
                String message = "Welcome to LambdaTest";
                
                System.out.println("3st");
                // Enter the string value in "Enter Message" text box
                page.locator("//input[@id='user-message']").fill(message);
                
                System.out.println("4st");
                // Click "Get Checked Value"
                page.locator("//button[@id='showInput']").click();

                // Validate whether the same message is displayed
                String displayedMessage = page.locator("#message").textContent();

                

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
	        result = page.evaluate("_ => {}", "lambdatest_action: { \"action\": \"setTestStatus\", \"arguments\": { \"status\": \"" + status + "\", \"remark\": \"" + remark + "\"}}");
	    }
	}
