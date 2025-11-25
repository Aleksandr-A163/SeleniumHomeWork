package di;

import com.google.inject.Guice;
import com.google.inject.Injector;
import driver.WebDriverProvider;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class GuiceExtension implements BeforeEachCallback, AfterEachCallback {

    private Injector injector;

    @Override
    public void beforeEach(ExtensionContext context) {
        injector = Guice.createInjector(new TestModule());
        injector.injectMembers(context.getRequiredTestInstance());
    }

    @Override
    public void afterEach(ExtensionContext context) {
        try {
            WebDriverProvider provider = injector.getInstance(WebDriverProvider.class);
            provider.quitDriver();
        } catch (Exception e) {
            System.err.println("[WARN] Failed to quit WebDriver: " + e.getMessage());
        }
    }
}
