package com.lambdatest;
import org.testng.AssertJUnit;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.AssertJUnit;
import com.google.gson.JsonObject;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.BoundingBox;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.Browser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class PlaywrightTestSingle2 {
	
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
               
                // 1. Open the website
                page.navigate("https://www.lambdatest.com/selenium-playground");
                
                System.out.println("1");
                page.click("//a[text()='Drag & Drop Sliders']");
               
                // Select the slider "Default value 15" and drag it to 95
                Locator slider = page.locator("input.sp__range[value='15']");
                slider.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
                slider.focus();
                                             
                for(int i=0; i<75; i++)
                {
                	page.keyboard().press("ArrowRight");
                	
                }
            
                Locator rangeSuccess = page.locator("#rangeSuccess");
                String rangeValue = rangeSuccess.textContent();
                System.out.println("Range Value: " + rangeValue); 
                                              
                int finalValue = Integer.parseInt(rangeValue.trim());
                assertEquals(90, finalValue, "The slider value should be 90.");
                // Optional Assertion

 if (finalValue == 90) {
	 setTestStatus("passed", "Title matched", page);
                System.out.println("✅ Test Passed: The slider value is 90.");
            } else {
            	setTestStatus("failed", "Title not matched", page);
                System.out.println("❌ Test Failed: Expected value 90, but got " + finalValue);
            }
 } catch (Exception err) {
                setTestStatus("failed", err.getMessage(), page);
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