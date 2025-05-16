package driver;

import com.google.inject.Provider;
import org.openqa.selenium.WebDriver;

public class WebDriverProvider implements Provider<WebDriver> {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    @Override
    public WebDriver get() {
        if (driverThreadLocal.get() == null) {
            driverThreadLocal.set(BrowserFactory.create());
        }
        return driverThreadLocal.get();
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
        }
    }
}