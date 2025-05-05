package driver;

import com.google.inject.Provider;
import com.google.inject.Singleton;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import utils.HighlightingListener;


/**
 * Guice Provider для WebDriver.
 * Создаёт ChromeDriver, оборачивает его в EventFiringDecorator
 * с HighlightingListener и предоставляет в DI.
 */
@Singleton
public class WebDriverProvider implements Provider<WebDriver> {

    @Override
    public WebDriver get() {
        WebDriverManager.chromedriver().setup();
        ChromeDriver raw = new ChromeDriver();
        return new EventFiringDecorator(new HighlightingListener())
                .decorate(raw);
    }
}