import com.google.gson.JsonObject;
import com.microsoft.playwright.Page;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(DataProviderRunner.class)
public class TestScenario1 extends BaseTest {
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
           
            page.locator("//a[text()='Simple Form Demo']").click();
            Thread.sleep(1000);
            String currentURL= page.url();
            assertTrue(currentURL.contains("simple-form-demo"));
            String value = "Welcome to LambdaTest";
            page.locator("//input[@placeholder='Please enter your Message']"). fill (value);
            Thread.sleep(1000);
            page.locator("//button[@id='showInput']").click();
            Thread.sleep(1000);
            String messageText = page.locator("//p[@id='message']").textContent();
            assertEquals(messageText, value);
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