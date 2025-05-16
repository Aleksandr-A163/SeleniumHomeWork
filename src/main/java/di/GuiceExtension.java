package di;

import com.google.inject.Guice;
import com.google.inject.Injector;
import driver.WebDriverProvider;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class GuiceExtension implements BeforeEachCallback, AfterEachCallback {

    private final Injector injector = Guice.createInjector(new TestModule());

    @Override
    public void beforeEach(ExtensionContext context) {
        injector.injectMembers(context.getRequiredTestInstance());
    }

    @Override
    public void afterEach(ExtensionContext context) {
        WebDriverProvider.quitDriver();
    }
}