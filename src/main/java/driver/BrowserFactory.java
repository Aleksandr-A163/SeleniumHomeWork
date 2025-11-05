package driver;

import org.openqa.selenium.WebDriver;

/**
 * Фабрика для создания драйверов.
 * Поддерживает локальный запуск и запуск через Selenoid.
 */
public class BrowserFactory {

    public static WebDriver create() {
        String runMode = System.getProperty("runMode", "local"); // local | selenoid
        String browserProperty = System.getProperty("browser", "chrome"); // chrome | firefox | edge | chromeMobile

        // ✅ Если указано selenoid — создаём драйвер через конфиг
        if ("selenoid".equalsIgnoreCase(runMode)) {
            switch (browserProperty.toLowerCase()) {
                case "chromemobile":
                    return SelenoidConfig.createChromeMobileIPhoneX();
                case "chrome":
                    return SelenoidConfig.createDesktopChrome();
                default:
                    throw new IllegalArgumentException(
                            "В режиме Selenoid поддерживаются только браузеры: chrome, chromeMobile");
            }
        }

        // ✅ По умолчанию — локальный запуск через WebDriverManager
        BrowserType browser = BrowserType.from(browserProperty);
        return browser.createDriver();
    }
}
