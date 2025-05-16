package scenarios;

import com.google.inject.Inject;
import di.GuiceExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import pages.MainPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Сценарий 3: выбор случайной категории через «Обучение»")
@Tag("category")
@ExtendWith(GuiceExtension.class)
public class NavigationMenuTest {

    @Inject
    private WebDriver driver;
    @Inject
    private MainPage mainPage;

    @Test
    void randomCategoryOpensCorrectCatalog() {
        mainPage.open();
        mainPage.openLearningMenu();
        String slug = mainPage.clickRandomCategory();

        String url = driver.getCurrentUrl();
        assertTrue(url.contains("categories=" + slug),
            "Ожидали в URL параметр categories=" + slug + ", но получили: " + url);
    }
}