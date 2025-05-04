package base;

import com.google.inject.Inject;
import di.InjectorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;

public abstract class BaseTest {

    @Inject
    protected WebDriver driver;

    @BeforeEach
    public void setUp() {
        InjectorFactory.getInjector().injectMembers(this);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}