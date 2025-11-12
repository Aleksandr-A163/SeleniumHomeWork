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

    /** Кнопка "Обучение" в хедере */
    private final By learningMenuButton = By.cssSelector("nav span[title='Обучение']");

    /** Кнопка бургера (для мобильной версии) */
    private final By burgerMenuButton = By.cssSelector("button[data-testid*='burger'], .header__burger, .hamburger");

    /** Ссылки на категории */
    private final By categoryLinkSelector = By.cssSelector("a[href*='/categories/']");

    @Inject
    public HeaderMenuComponent(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    /** Открывает меню «Обучение», включая бургер в мобильной версии */
    public void openLearningMenu() {
        try {
            // если бургер есть — открываем его
            List<WebElement> burgers = driver.findElements(burgerMenuButton);
            if (!burgers.isEmpty()) {
                WebElement burger = burgers.get(0);
                if (burger.isDisplayed()) {
                    burger.click();
                    System.out.println("🍔 Открыто бургер-меню (мобильная версия)");
                    Thread.sleep(500);
                }
            }
        } catch (InterruptedException ignored) {}

        // открываем "Обучение"
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(learningMenuButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
        btn.click();

        // ждём появления категорий
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(categoryLinkSelector, 0));
    }

    /** Кликает по случайной категории и возвращает её slug */
    public String clickRandomCategory() {
        // Убедимся, что меню открыто
        openLearningMenu();

        // Ждём появления ссылок
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(categoryLinkSelector));
        List<WebElement> links = driver.findElements(categoryLinkSelector);

        if (links.isEmpty()) {
            throw new NoSuchElementException("❌ Категории не найдены — меню 'Обучение' пустое");
        }

        Random random = new Random();
        WebElement link = links.get(random.nextInt(links.size()));
        String href = link.getAttribute("href");

        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", link);
                wait.until(ExpectedConditions.elementToBeClickable(link));
                link.click();
                System.out.println("✅ Клик по категории: " + href);
                return href.substring(href.lastIndexOf('/') + 1);
            } catch (Exception e) {
                System.out.println("⚠️ Попытка " + attempt + " неудачна: " + e.getClass().getSimpleName());
                links = driver.findElements(categoryLinkSelector);
                if (!links.isEmpty()) {
                    link = links.get(random.nextInt(links.size()));
                    href = link.getAttribute("href");
                }
            }
        }

        throw new RuntimeException("❌ Не удалось кликнуть по категории после 3 попыток");
    }
}
