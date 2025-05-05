package di;

import com.google.inject.Injector;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;

public class GuiceExtension implements BeforeEachCallback, AfterEachCallback {
    private final Injector injector = InjectorFactory.getInjector();

    @Override
    public void beforeEach(ExtensionContext ctx) {
        Object testInstance = ctx.getRequiredTestInstance();
        injector.injectMembers(testInstance);
    }

    @Override
    public void afterEach(ExtensionContext ctx) {
        // получаем инжектированный WebDriver и завершаем его
        WebDriver driver = injector.getInstance(WebDriver.class);
        if (driver != null) {
            driver.quit();
        }
    }
}