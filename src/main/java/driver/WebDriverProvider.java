package driver;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.openqa.selenium.WebDriver;

public class WebDriverProvider implements Provider<WebDriver> {

    private final BrowserFactory browserFactory;
    private WebDriver driver;   // <-- хранение инстанса

    @Inject
    public WebDriverProvider(BrowserFactory browserFactory) {
        this.browserFactory = browserFactory;
    }

    @Override
    public WebDriver get() {
        if (driver == null) {
            String browserProp = System.getProperty("browser", "chrome");
            String runModeProp = System.getProperty("runMode", "local");

            BrowserType browser = BrowserType.from(browserProp);
            RunMode runMode = RunMode.from(runModeProp);

            driver = browserFactory.create(browser, runMode);
        }
        return driver;
    }

    public void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
