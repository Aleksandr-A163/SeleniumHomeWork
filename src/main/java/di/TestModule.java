package di;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import driver.WebDriverProvider;
import org.openqa.selenium.WebDriver;
import pages.MainPage;
import pages.CourseCatalogPage;
import pages.CoursePage;
import components.HeaderMenuComponent;
import components.CourseListComponent;

/**
 * Guice-модуль для инициализации зависимостей WebDriver и PageObject-классов.
 */
public class TestModule extends AbstractModule {

    @Override
    protected void configure() {
        // пусто — все бинды через @Provides
    }

    @Provides
    @Singleton
    WebDriver provideWebDriver() {
        return new WebDriverProvider().get();
    }

    @Provides
    HeaderMenuComponent provideHeaderMenuComponent(WebDriver driver) {
        return new HeaderMenuComponent(driver);
    }

    @Provides
    CourseListComponent provideCourseListComponent(WebDriver driver) {
        return new CourseListComponent(driver);
    }

    @Provides
    MainPage provideMainPage(WebDriver driver, HeaderMenuComponent header) {
        return new MainPage(driver, header);
    }

    @Provides
    CourseCatalogPage provideCourseCatalogPage(WebDriver driver, CourseListComponent list) {
        return new CourseCatalogPage(driver, list);
    }

    @Provides
    CoursePage provideCoursePage(WebDriver driver, HeaderMenuComponent header) {
        return new CoursePage(driver);
    }
}
