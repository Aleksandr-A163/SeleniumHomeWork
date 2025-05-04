package components;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

public class CourseCardComponent {

    private final WebDriver driver;
    private final WebElement root;

    private final By titleSelector = By.cssSelector("h6 > div");
    private final By dateSelector = By.xpath(".//div[contains(@class,'ieVVRJ')]"); // Блок с датой

    public CourseCardComponent(WebDriver driver, WebElement root) {
        this.driver = driver;
        this.root = root;
    }

    public String getTitle() {
        return root.findElement(titleSelector).getText().trim();
    }

    public void click() {
        try {
            root.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            System.out.println("Клик по курсу перехвачен другим элементом, используем JavaScript.");
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", root);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", root);
        }
    }

    /**
     * Безопасное получение даты старта. Возвращает Optional.empty(), если дата не найдена или некорректна.
     */
public Optional<LocalDate> tryGetStartDate() {
    try {
        WebElement dateElement = root.findElement(dateSelector);
        String dateText = dateElement.getText().trim();

        if (dateText.isBlank()) {
            System.out.println("⛔ Курс \"" + getTitle() + "\" не содержит дату старта.");
            return Optional.empty();
        }

        // Нормализуем пробелы (замена неразрывного на обычный)
        dateText = dateText.replace('\u00A0', ' ').replace("\u202F", " ");

        // Удаляем всё после запятой с числом месяца и года
        // Пример: "24 апреля, 2025 · 5 месяцев" → "24 апреля, 2025"
        String[] parts = dateText.split("·");
        String datePart = parts[0].trim();

        // Проверка: содержит ли дата ожидаемый формат
        if (!datePart.matches(".*\\d{4}")) {
            System.out.println("⛔ Курс \"" + getTitle() + "\" не содержит валидную дату старта.");
            return Optional.empty();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM, yyyy", new Locale("ru"));
        LocalDate parsedDate = LocalDate.parse(datePart, formatter);
        return Optional.of(parsedDate);

    } catch (Exception e) {
        System.out.println("⛔ Ошибка при парсинге даты для курса \"" + getTitle() + "\": " + e.getMessage());
        return Optional.empty();
    }
}

}