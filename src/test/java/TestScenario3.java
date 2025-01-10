import com.google.gson.JsonObject;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(DataProviderRunner.class)
public class TestScenario3 extends BaseTest {
    String testURL = "https://www.lambdatest.com/selenium-playground";

    @Test
    @UseDataProvider(value = "getDefaultTestCapability", location = LTCapability.class)
    public void testScenario(JsonObject capability) throws Exception {
        Driver driver = null;
        Page page = null;
        try {
            driver = super.createConnection(capability);
            page = driver.getPage();
            page.navigate(testURL);
            Thread.sleep(2000);
            page.setViewportSize(1900, 1050);
           
            page.locator("//a[contains(text(),'Input Form Submit')]").click();
            Thread.sleep(1000);

            page.locator("//button[text()='Submit']").click();
            Thread.sleep(1000);

            Locator validationMessage = page.locator("input:has-text('Please fill out this field.')");
            if (validationMessage.isVisible()) {
                System.out.println("Validation message is displayed: 'Please fill out this field'");
            } else {
                System.out.println("Validation message not displayed.");
            }                

            page.locator("//input[@placeholder='Name']").fill("John");
            page.locator("//input[@placeholder='Email']").fill("john@example.com");
            Thread.sleep(1000);
    
            page.locator("//input[@placeholder='Password']").fill("abc123");
            page.locator("//input[@placeholder='Company']").fill("Kyle Ltd");
            Thread.sleep(1000);
            
            page.locator("//input[@placeholder='Website']").fill("Google");
            page.locator("//input[@placeholder='City']").fill("Arizona");
            page.locator("//select[@name='country']").selectOption("US");
            Thread.sleep(1000);

            page.locator("//input[@placeholder='Address 1']").fill("State park");
            page.locator("//input[@placeholder='Address 2']").fill("Central Area");
            Thread.sleep(1000);

            page.locator("//input[@placeholder='State']").fill("Texas");
            page.locator("//input[@placeholder='Zip code']").fill("41098");
            
            page.locator("//button[text()='Submit']").click();
            Thread.sleep(1000);

            String successMsg = page.locator("[class='success-msg hidden']").textContent();
            String expectedMsg = "Thanks for contacting us, we will get back to you shortly.";

            assertEquals(successMsg, expectedMsg);

            Thread.sleep(2000);
            if (page.title().equalsIgnoreCase("Selenium Grid Online | Run Selenium Test on Cloud")) {
                super.setTestStatus("passed", "Title matched", page);
            }
            else {
                super.setTestStatus("failed", "Title not matched", page);
            }
            super.closeConnection(driver);
        } catch (Exception err) {
            err.printStackTrace();
            super.setTestStatus("FAILED", err.getMessage(), page);
            super.closeConnection(driver);
            throw err;
        }
    }
}