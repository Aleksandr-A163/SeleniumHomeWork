package di;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import driver.WebDriverProvider;
import org.openqa.selenium.WebDriver;
import pages.MainPage;
import pages.CourseCatalogPage;
import pages.CoursePage;
import components.HeaderMenuComponent;
import components.CourseListComponent;

public class TestModule extends AbstractModule {

    @Override
    protected void configure() {
        // Просто регистрируем классы, без биндов
        bind(MainPage.class);
        bind(CourseCatalogPage.class);
        bind(CoursePage.class);

        bind(HeaderMenuComponent.class);
        bind(CourseListComponent.class);
    }

    @Provides
    WebDriver provideWebDriver() {
        return new WebDriverProvider().get();
    }
}