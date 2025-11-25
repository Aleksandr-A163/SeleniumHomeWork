package di;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import driver.BrowserFactory;
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
        bind(BrowserFactory.class).asEagerSingleton();
        bind(WebDriverProvider.class).asEagerSingleton();
    }

    @Provides
    @Singleton
    public WebDriver provideWebDriver(WebDriverProvider provider) {
        return provider.get();
    }

    // --- Components ---

    @Provides
    public HeaderMenuComponent provideHeaderMenuComponent(WebDriver driver) {
        return new HeaderMenuComponent(driver);
    }

    @Provides
    public CourseListComponent provideCourseListComponent(WebDriver driver) {
        return new CourseListComponent(driver);
    }

    // --- Pages ---

    @Provides
    public MainPage provideMainPage(WebDriver driver,
                                    HeaderMenuComponent headerMenuComponent) {
        return new MainPage(driver, headerMenuComponent);
    }

    @Provides
    public CourseCatalogPage provideCourseCatalogPage(WebDriver driver,
                                                      CourseListComponent courseListComponent) {
        return new CourseCatalogPage(driver, courseListComponent);
    }

    @Provides
    public CoursePage provideCoursePage(WebDriver driver) {
        return new CoursePage(driver);
    }
}
