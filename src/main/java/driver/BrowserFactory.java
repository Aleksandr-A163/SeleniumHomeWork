package driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class BrowserFactory {

    public WebDriver create(BrowserType browser, RunMode runMode) {
        return switch (runMode) {
            case LOCAL -> createLocal(browser);
            case SELENOID, GGR -> createRemote(browser, runMode);
        };
    }

    private WebDriver createLocal(BrowserType browser) {
        switch (browser) {
            case CHROME, CHROME_MOBILE -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--remote-allow-origins=*");
                options.addArguments("--start-maximized");

                if (browser == BrowserType.CHROME_MOBILE) {
                    // простая mobile-эмуляция локально, чтобы поведение совпадало
                    options.setExperimentalOption(
                            "mobileEmulation",
                            java.util.Map.of("deviceName", "iPhone X")
                    );
                }

                return new ChromeDriver(options);
            }
            default ->
                    throw new IllegalArgumentException("Local run is not supported for: " + browser);
        }
    }

    private WebDriver createRemote(BrowserType browser, RunMode runMode) {
        return switch (browser) {
            case CHROME -> SelenoidConfig.createDesktop(runMode);
            case CHROME_MOBILE -> SelenoidConfig.createMobile(runMode);
        };
    }
}
