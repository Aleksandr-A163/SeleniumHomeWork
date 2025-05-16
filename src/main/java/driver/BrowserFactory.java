package driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class BrowserFactory {

    public static WebDriver create() {
        String browserProperty = System.getProperty("browser");
        BrowserType browser = BrowserType.from(browserProperty);

        switch (browser) {
            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                System.out.println("[INFO] Используется браузер: Firefox");
                return new FirefoxDriver();

            case EDGE:
                WebDriverManager.edgedriver().setup();
                System.out.println("[INFO] Используется браузер: Edge");
                return new EdgeDriver();

            case CHROME:
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--start-maximized");
                System.out.println("[INFO] Используется браузер: Chrome");
                return new ChromeDriver(options);
        }
    }
}