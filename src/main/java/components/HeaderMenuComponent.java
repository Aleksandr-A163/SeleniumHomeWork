package components;

import com.google.inject.Inject;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Random;

/**
 * Компонент хедера с меню «Обучение».
 */
public class HeaderMenuComponent {

    private final WebDriver driver;
    private final WebDriverWait wait;

    /** Кнопка «Обучение» в хедере */
    private final By learningMenuButton = By.cssSelector("nav span[title='Обучение']");

    /** Ссылки на категории (устойчивый локатор, без динамических классов) */
    private final By categoryLinkSelector = By.cssSelector("a[href*='/categories/']");

    @Inject
    public HeaderMenuComponent(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /** Открывает меню «Обучение» */
    public void openLearningMenu() {
        // Дожидаемся кликабельности и кликаем
        WebElement menuButton = wait.until(ExpectedConditions.elementToBeClickable(learningMenuButton));
        menuButton.click();

        // Ждём появления хотя бы одной категории
        wait.until(ExpectedConditions.presenceOfElementLocated(categoryLinkSelector));
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(categoryLinkSelector));
    }

    /** Кликает по случайной категории и возвращает её slug */
    public String clickRandomCategory() {
        List<WebElement> links = wait.until(
            ExpectedConditions.visibilityOfAllElementsLocatedBy(categoryLinkSelector));

        if (links.isEmpty()) {
            throw new NoSuchElementException("Не найдены ссылки категорий по локатору " + categoryLinkSelector);
        }

        WebElement link = links.get(new Random().nextInt(links.size()));
        String href = link.getAttribute("href");
        String slug = href.substring(href.lastIndexOf('/') + 1);
        link.click();
        return slug;
    }
}
