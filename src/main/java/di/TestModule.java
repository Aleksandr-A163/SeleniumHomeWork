package di;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import driver.WebDriverProvider;
import org.openqa.selenium.WebDriver;
import pages.CourseCatalogPage;
import pages.CoursePage;

public class TestModule extends AbstractModule {
    @Override
    protected void configure() {
        // WebDriver через наш провайдер
        bind(WebDriver.class)
            .toProvider(WebDriverProvider.class)
            .in(Singleton.class);

        // Страницы
        bind(CourseCatalogPage.class).in(Singleton.class);
        bind(CoursePage.class).in(Singleton.class);
    }
}