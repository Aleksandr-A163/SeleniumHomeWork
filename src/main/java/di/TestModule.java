package di;

import com.google.inject.AbstractModule;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

public class TestModule extends AbstractModule {

    @Override
    protected void configure() {
        WebDriverManager.chromedriver().setup();
        bind(WebDriver.class).toInstance(new ChromeDriver());
    }
}