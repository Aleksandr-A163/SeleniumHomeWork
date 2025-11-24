package driver;

import com.google.inject.Provider;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import utils.HighlightingListener;

public class WebDriverProvider implements Provider<WebDriver> {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    @Override
    public WebDriver get() {
        if (DRIVER.get() == null) {
            WebDriver base = BrowserFactory.create();
            WebDriver decorated = new EventFiringDecorator<>(new HighlightingListener()).decorate(base);
            DRIVER.set(decorated);
        }
        return DRIVER.get();
    }

    public static void quitDriver() {
        WebDriver drv = DRIVER.get();
        if (drv != null) {
            drv.quit();
            DRIVER.remove();
        }
    }
}
