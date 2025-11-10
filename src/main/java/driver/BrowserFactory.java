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

        System.out.printf("🚀 Запуск тестов: mode=%s, browser=%s%n", runMode, browserProperty);

        // --- Запуск через Selenoid ---
        if ("selenoid".equalsIgnoreCase(runMode)) {
            switch (browserProperty.toLowerCase()) {
                case "chromemobile":
                case "chrome_mobile":
                    System.out.println("🧭 Используется Selenoid mobile Chrome (iPhone X)");
                    return SelenoidConfig.createChromeMobileIPhoneX();

                case "chrome":
                    System.out.println("🌐 Используется Selenoid desktop Chrome");
                    return SelenoidConfig.createDesktopChrome();

                default:
                    throw new IllegalArgumentException(
                        "В режиме Selenoid поддерживаются только браузеры: chrome, chromeMobile");
            }
        }

        // --- Локальный запуск ---
        BrowserType browser = BrowserType.from(browserProperty);
        System.out.println("🖥 Локальный запуск браузера: " + browser.name());
        return browser.createDriver();
    }
}
