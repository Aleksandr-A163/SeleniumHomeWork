package driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public enum BrowserType {

    CHROME {
        @Override
        public WebDriver createLocal() {
            WebDriverManager.chromedriver().setup();
            ChromeOptions opt = new ChromeOptions();
            opt.addArguments("--start-maximized");
            return new ChromeDriver(opt);
        }
    },

    FIREFOX {
        @Override
        public WebDriver createLocal() {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions opt = new FirefoxOptions();
            opt.addArguments("--start-maximized");
            return new FirefoxDriver(opt);
        }
    },

    EDGE {
        @Override
        public WebDriver createLocal() {
            WebDriverManager.edgedriver().setup();
            EdgeOptions opt = new EdgeOptions();
            opt.addArguments("--start-maximized");
            return new EdgeDriver(opt);
        }
    },

    CHROME_MOBILE {
        @Override
        public WebDriver createLocal() {
            throw new UnsupportedOperationException(
                "Мобильный Chrome доступен только через Selenoid/GGR"
            );
        }
    };

    public abstract WebDriver createLocal();

    public static BrowserType fromProperty() {
        String value = System.getProperty("browser", "chrome").toLowerCase();
        return switch (value) {
            case "firefox" -> FIREFOX;
            case "edge" -> EDGE;
            case "chromemobile", "chrome_mobile" -> CHROME_MOBILE;
            default -> CHROME;
        };
    }
}
