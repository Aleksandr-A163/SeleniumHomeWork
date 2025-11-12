package pages;

import com.google.inject.Inject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException; // ✅ добавлен импорт
import java.util.Locale;

/**
 * Страница курса Otus.
 */
public class CoursePage extends BasePage {

    private final WebDriverWait wait;

    // Селектор заголовка курса
    private final By titleSelector = By.cssSelector("h1.diGrSa");

    // Селектор для даты начала курса (может меняться на Otus)
    private static final String DATE_P_SELECTOR = "p[class*='sc-1x9oq14-0']";

    @Inject
    public CoursePage(WebDriver driver) {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @Override
    protected String getPath() {
        return "/"; // при необходимости можно переопределить
    }

    public String getCourseTitle() {
        WebElement titleElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(titleSelector)
        );
        return titleElement.getText().trim();
    }

    public boolean isCorrectCourseOpened(String expectedTitle) {
        return getCourseTitle().equalsIgnoreCase(expectedTitle);
    }

    /**
     * Получает дату старта курса. Без падения на пустых или отсутствующих элементах.
     */
    public LocalDate getCourseStartDateJsoup() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(DATE_P_SELECTOR)));

            WebElement dateElement = driver.findElement(By.cssSelector(DATE_P_SELECTOR));
            String text = dateElement.getText().trim();

            if (text.isEmpty()) {
                System.err.println("⚠️ На странице курса отсутствует дата старта");
                return null;
            }

            // Приводим к формату "29 октября, 2025"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM, yyyy", new Locale("ru"));

            // Иногда год отсутствует, тогда добавляем текущий
            if (!text.matches(".*\\d{4}.*")) {
                text = text + ", " + LocalDate.now().getYear();
            }

            return LocalDate.parse(text, formatter);

        } catch (TimeoutException | NoSuchElementException e) {
            System.err.println("⚠️ Элемент даты курса не найден: " + e.getMessage());
            return null;
        } catch (DateTimeParseException e) {
            System.err.println("⚠️ Ошибка при парсинге даты: " + e.getParsedString());
            return null;
        }
    }
}
