import com.google.gson.JsonObject;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(DataProviderRunner.class)
public class TestScenario2 extends BaseTest {
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
           
            page.locator("//a[text()='Drag & Drop Sliders']").click();
            Thread.sleep(2000);
            Locator sliderValue = page.locator("//input[@type='range' and @value='15']");
            Locator outputValue = page.locator("//output[@id='rangeSuccess']");
            double slidermove = 0;
            for(int i=1;i<=31;i++){
                BoundingBox boundingbox = sliderValue.boundingBox();
                page.mouse().move(boundingbox.x + slidermove, boundingbox.y);
                page.mouse().down();
                slidermove += 15;
                page.mouse().move(boundingbox.x + slidermove, boundingbox.y);
                page.mouse().up();
            }
            String updatedOutputValue = outputValue.textContent();
            System.out.println("Updated output value : " + updatedOutputValue);
            if (updatedOutputValue.equals("95")) {
                System.out.println("Test Passed: Output value is correctly updated to 95.");
            } else {
                System.out.println("Test Failed: Output value is " + updatedOutputValue + ", but expected 95.");
            }

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