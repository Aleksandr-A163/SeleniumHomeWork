package driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Selenoid connection helper.
 * Usage is controlled via -DrunMode=selenoid and -Dbrowser=chrome|chromeMobile
 */
public class SelenoidConfig {

    private static URL hubUrl() {
        try {
            // Windows / Mac: host.docker.internal, Linux: 127.0.0.1
            String hub = System.getProperty("selenoid.hub", "http://host.docker.internal:4444/wd/hub");
            return new URL(hub);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Selenoid hub URL", e);
        }
    }

    /** Desktop Chrome configuration */
    public static WebDriver createDesktopChrome() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        Map<String, Object> selenoidOptions = new HashMap<>();
        selenoidOptions.put("enableVNC", true);
        selenoidOptions.put("enableVideo", false);
        selenoidOptions.put("sessionTimeout", "3m");
        selenoidOptions.put("name", "Desktop Chrome test");

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(ChromeOptions.CAPABILITY, options);
        caps.setCapability("browserName", "chrome");
        caps.setCapability("browserVersion", "122.0"); // ✅ должна совпадать с browsers.json
        caps.setCapability("selenoid:options", selenoidOptions);

        return new RemoteWebDriver(hubUrl(), caps);
    }

    /** Mobile Chrome configuration (iPhone X emulation) */
    public static WebDriver createChromeMobileIPhoneX() {
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> mobileEmu = new HashMap<>();
        mobileEmu.put("deviceName", "iPhone X");
        options.setExperimentalOption("mobileEmulation", mobileEmu);
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        Map<String, Object> selenoidOptions = new HashMap<>();
        selenoidOptions.put("enableVNC", true);
        selenoidOptions.put("enableVideo", false);
        selenoidOptions.put("sessionTimeout", "3m");
        selenoidOptions.put("name", "Mobile Chrome (iPhone X)");

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(ChromeOptions.CAPABILITY, options);
        caps.setCapability("browserName", "chrome");
        caps.setCapability("browserVersion", "122.0"); // ✅ синхронизировано с config/browsers.json
        caps.setCapability("selenoid:options", selenoidOptions);

        return new RemoteWebDriver(hubUrl(), caps);
    }
}
