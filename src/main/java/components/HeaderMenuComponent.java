package components;

import com.google.inject.Inject;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
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
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(learningMenuButton));
        btn.click();
        // ждём появления хотя бы одной видимой ссылки категории
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(categoryLinkSelector, 0));
    }

    /** Кликает по случайной категории и возвращает её slug */
    public String clickRandomCategory() {
        // Ждём пока меню точно откроется и появятся ссылки
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(categoryLinkSelector));

        List<WebElement> links = driver.findElements(categoryLinkSelector);

        // Проверка на случай пустого списка
        if (links.isEmpty()) {
            throw new NoSuchElementException("❌ Категории не найдены — возможно, меню 'Обучение' не открылось");
        }

        Random random = new Random();
        WebElement link = links.get(random.nextInt(links.size()));

        String href = link.getAttribute("href"); // сохраняем slug ДО клика

        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", link);
                wait.until(ExpectedConditions.elementToBeClickable(link));
                link.click();
                return href.substring(href.lastIndexOf('/') + 1);
            } catch (StaleElementReferenceException e) {
                System.out.println("⚠️ DOM обновился, пробуем снова... (" + attempt + "/3)");
                links = driver.findElements(categoryLinkSelector);
                if (!links.isEmpty()) {
                    link = links.get(random.nextInt(links.size()));
                    href = link.getAttribute("href");
                }
            } catch (ElementClickInterceptedException e) {
                System.out.println("⚠️ Элемент перекрыт — повторный клик...");
                new Actions(driver).moveToElement(link).pause(Duration.ofMillis(200)).click().perform();
                return href.substring(href.lastIndexOf('/') + 1);
            }
        }

        throw new RuntimeException("❌ Не удалось кликнуть по категории после нескольких попыток");
    }


}
