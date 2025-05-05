package driver;

import com.google.inject.Provider;
import com.google.inject.Singleton;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.events.EventFiringDecorator;
import utils.HighlightingListener;

/**
 * Guice Provider для WebDriver с кэшированием на уровне потока.
 */
@Singleton
public class WebDriverProvider implements Provider<WebDriver> {

    // Каждый поток (тест) будет держать свой драйвер
    private static final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    @Override
    public WebDriver get() {
        WebDriver driver = tlDriver.get();
        if (driver == null) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            ChromeDriver raw = new ChromeDriver(options);
            driver = new EventFiringDecorator(new HighlightingListener())
                         .decorate(raw);
            tlDriver.set(driver);
        }
        return driver;
    }

    /** Закрывает драйвер текущего потока и очищает ThreadLocal */
    public static void quitDriver() {
        WebDriver driver = tlDriver.get();
        if (driver != null) {
            driver.quit();
            tlDriver.remove();
        }
    }
}