package driver;

import org.openqa.selenium.WebDriver;
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
            // host.docker.internal is valid on Win/Mac. On Linux, use 127.0.0.1
            String hub = System.getProperty("selenoid.hub", "http://host.docker.internal:4444/wd/hub");
            return new URL(hub);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Bad Selenoid hub URL", e);
        }
    }

    public static WebDriver createDesktopChrome() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setBrowserName("chrome");
        caps.setCapability("enableVNC", true);
        caps.setCapability("enableVideo", true);
        return new RemoteWebDriver(hubUrl(), caps);
    }

    public static WebDriver createChromeMobileIPhoneX() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setBrowserName("chrome");
        caps.setCapability("enableVNC", true);
        caps.setCapability("enableVideo", true);

        Map<String, Object> mobileEmu = new HashMap<>();
        mobileEmu.put("deviceName", "iPhone X");
        Map<String, Object> chromeOpts = new HashMap<>();
        chromeOpts.put("mobileEmulation", mobileEmu);
        caps.setCapability("goog:chromeOptions", chromeOpts);

        return new RemoteWebDriver(hubUrl(), caps);
    }
}
