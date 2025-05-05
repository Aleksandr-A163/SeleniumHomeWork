package di;

import com.google.inject.Injector;
import driver.WebDriverProvider;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class GuiceExtension implements BeforeEachCallback, AfterEachCallback {
    private final Injector injector = InjectorFactory.getInjector();

    @Override
    public void beforeEach(ExtensionContext ctx) {
        injector.injectMembers(ctx.getRequiredTestInstance());
    }

    @Override
    public void afterEach(ExtensionContext ctx) {
        // вместо закрытия синглтон-драйвера, закрываем потоковый
        WebDriverProvider.quitDriver();
    }
}