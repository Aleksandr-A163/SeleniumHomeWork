package driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SelenoidConfig {

    private static URL hub(RunMode runMode) {
        try {
            return switch (runMode) {
                case SELENOID -> new URL("http://localhost:4444/wd/hub");
                case GGR -> new URL("http://localhost:4445/wd/hub");
                default -> throw new IllegalStateException("Hub not required for LOCAL");
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static ChromeOptions baseChrome() {
        ChromeOptions opt = new ChromeOptions();
        opt.addArguments("--no-sandbox");
        opt.addArguments("--disable-dev-shm-usage");
        return opt;
    }

    public static WebDriver createDesktop(RunMode runMode) {
        ChromeOptions opt = baseChrome();

        Map<String, Object> selenoidOptions = new HashMap<>();
        selenoidOptions.put("enableVNC", true);
        selenoidOptions.put("sessionTimeout", "3m");
        opt.setCapability("selenoid:options", selenoidOptions);

        opt.setBrowserVersion("latest");
        opt.setCapability("browserName", "chrome");

        return new RemoteWebDriver(hub(runMode), opt);
    }

    public static WebDriver createMobile(RunMode runMode) {
        ChromeOptions opt = baseChrome();

        Map<String, Object> mobile = new HashMap<>();
        mobile.put("deviceName", "iPhone X");
        opt.setExperimentalOption("mobileEmulation", mobile);

        Map<String, Object> selenoidOptions = new HashMap<>();
        selenoidOptions.put("enableVNC", true);
        selenoidOptions.put("sessionTimeout", "3m");
        opt.setCapability("selenoid:options", selenoidOptions);

        opt.setBrowserVersion("latest");
        opt.setCapability("browserName", "chrome");

        return new RemoteWebDriver(hub(runMode), opt);
    }
}
