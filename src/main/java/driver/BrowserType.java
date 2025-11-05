package driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Определяет тип браузера и создаёт соответствующий WebDriver.
 * Теперь поддерживает и эмуляцию мобильного Chrome через Selenoid.
 */
public enum BrowserType {

    CHROME {
        @Override
        public WebDriver createDriver() {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            return new ChromeDriver(options);
        }
    },

    FIREFOX {
        @Override
        public WebDriver createDriver() {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--start-maximized");
            return new FirefoxDriver(options);
        }
    },

    EDGE {
        @Override
        public WebDriver createDriver() {
            WebDriverManager.edgedriver().setup();
            EdgeOptions options = new EdgeOptions();
            options.addArguments("--start-maximized");
            return new EdgeDriver(options);
        }
    },

    CHROME_MOBILE {
        @Override
        public WebDriver createDriver() {
            // ✅ Используем SelenoidConfig для мобильной эмуляции
            return SelenoidConfig.createChromeMobileIPhoneX();
        }
    };

    public abstract WebDriver createDriver();

    public static BrowserType from(String name) {
        if (name == null || name.isBlank()) {
            return CHROME; // По умолчанию
        }

        try {
            return BrowserType.valueOf(name.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "Неподдерживаемый браузер: " + name +
                ". Допустимые значения: chrome, firefox, edge, chromeMobile."
            );
        }
    }
}
