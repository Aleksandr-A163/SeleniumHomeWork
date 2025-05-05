package driver;

import com.google.inject.Provider;
import com.google.inject.Singleton;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.events.EventFiringDecorator;
import utils.HighlightingListener;

@Singleton
public class WebDriverProvider implements Provider<WebDriver> {

    @Override
    public WebDriver get() {
        // 1) Подготовка драйвера
        WebDriverManager.chromedriver().setup();

        // 2) Задаём опции для запуска во весь экран
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        // если хотите строго указать размеры, можно так:
        // options.addArguments("--window-size=1920,1080");

        // 3) Создаём ChromeDriver с этими опциями
        ChromeDriver raw = new ChromeDriver(options);

        // 4) Оборачиваем в декоратор для подсветки
        return new EventFiringDecorator(new HighlightingListener())
                .decorate(raw);
    }
}