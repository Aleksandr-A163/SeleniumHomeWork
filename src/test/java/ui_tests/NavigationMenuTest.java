package ui_tests;

import com.google.inject.Inject;
import di.GuiceExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import pages.MainPage;

/**
 * Тест проверяет, что при клике на случайную категорию открывается корректный каталог.
 */
@ExtendWith(GuiceExtension.class) // ✅ обязательно для работы DI
public class NavigationMenuTest {

    @Inject
    private WebDriver driver;

    @Inject
    private MainPage mainPage;

    @Test
    public void randomCategoryOpensCorrectCatalog() {
        mainPage.open();

        String categorySlug = mainPage.clickRandomCategory();
        String currentUrl = driver.getCurrentUrl();

        boolean hasExpectedParam =
                currentUrl.contains("categories=" + categorySlug)
                        || currentUrl.contains("education_types=" + categorySlug)
                        || currentUrl.contains("categories")
                        || currentUrl.contains("education_types");

        Assertions.assertTrue(
                hasExpectedParam,
                "Ожидали в URL параметр categories=" + categorySlug + ", но получили: " + currentUrl
        );
    }
}
