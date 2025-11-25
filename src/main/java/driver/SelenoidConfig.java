package driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class SelenoidConfig {

    // -------------------------
    // Base Chrome Options
    // -------------------------
    private static ChromeOptions baseChrome() {
        ChromeOptions opt = new ChromeOptions();
        opt.addArguments("--no-sandbox");
        opt.addArguments("--disable-dev-shm-usage");
        opt.addArguments("--disable-gpu");
        opt.addArguments("--disable-notifications");
        opt.addArguments("--disable-infobars");
        opt.setCapability("acceptInsecureCerts", true);
        return opt;
    }

    // -------------------------
    // Build HUB URL (fixed)
    // -------------------------
    private static URL hub(RunMode mode) {
        try {
            if (mode == RunMode.LOCAL) {
                return null; // Local ChromeDriver mode
            }

            // Stable and correct for Windows, WSL2, Linux, Docker Desktop
            return new URL("http://localhost:4444/wd/hub");

        } catch (Exception e) {
            throw new RuntimeException("Failed to resolve Selenoid hub URL", e);
        }
    }

    // -------------------------
    // DESKTOP
    // -------------------------
    public static WebDriver createDesktop(RunMode runMode) {
        ChromeOptions opt = baseChrome();

        Map<String, Object> selenoid = new HashMap<>();
        selenoid.put("enableVNC", true);
        selenoid.put("enableVideo", false);
        opt.setCapability("selenoid:options", selenoid);

        RemoteWebDriver driver = new RemoteWebDriver(
                hub(runMode),
                opt
        );

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(25));
        return driver;
    }

    // -------------------------
    // MOBILE
    // -------------------------
    public static WebDriver createMobile(RunMode runMode) {
        ChromeOptions opt = baseChrome();

        Map<String, Object> mobile = new HashMap<>();
        mobile.put("deviceName", "iPhone X");
        opt.setExperimentalOption("mobileEmulation", mobile);

        Map<String, Object> selenoid = new HashMap<>();
        selenoid.put("enableVNC", true);
        selenoid.put("enableVideo", false);
        opt.setCapability("selenoid:options", selenoid);

        RemoteWebDriver driver = new RemoteWebDriver(
                hub(runMode),
                opt
        );

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(25));
        return driver;
    }
}
