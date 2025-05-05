package di;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import pages.CourseCatalogPage;
import pages.CoursePage;

public class TestModule extends AbstractModule {

    @Override
    protected void configure() {
        // WebDriver
        WebDriverManager.chromedriver().setup();
        bind(WebDriver.class)
            .toInstance(new ChromeDriver());

        // Pages
        bind(CourseCatalogPage.class).in(Singleton.class);
        bind(CoursePage.class).in(Singleton.class);

        // Убираем биндинг для CourseCardComponent!
        // bind(CourseCardComponent.class);
    }
}