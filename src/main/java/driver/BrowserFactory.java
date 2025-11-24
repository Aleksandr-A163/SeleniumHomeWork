package driver;

import org.openqa.selenium.WebDriver;

public class BrowserFactory {

    public static WebDriver create() {
        RunMode mode = RunMode.fromProperty();
        BrowserType browser = BrowserType.fromProperty();

        System.out.printf("▶ runMode=%s, browser=%s%n", mode, browser);

        return switch (mode) {
            case LOCAL -> browser.createLocal();

            case SELENOID -> {
                if (browser == BrowserType.CHROME_MOBILE)
                    yield SelenoidConfig.createMobile(mode);
                yield SelenoidConfig.createDesktop(mode);
            }

            case GGR -> {
                if (browser == BrowserType.CHROME_MOBILE)
                    yield SelenoidConfig.createMobile(mode);
                yield SelenoidConfig.createDesktop(mode);
            }
        };
    }
}
