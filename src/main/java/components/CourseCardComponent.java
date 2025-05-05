package components;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Optional;

public class CourseCardComponent {
    private final WebElement root;
    private final WebDriver driver;

    private final By titleSelector = By.cssSelector("h6 > div");
    private final By dateTextSelector = By.cssSelector("div[class*='sc-157icee-1'] > div");

    public CourseCardComponent(WebDriver driver, WebElement root) {
        this.driver = driver;
        this.root = root;
    }

    public String getTitle() {
        try {
            return root.findElement(titleSelector).getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public Optional<LocalDate> tryGetStartDate() {
        try {
            String fullText = root.findElement(dateTextSelector).getText().trim();
            String datePart = fullText.split(" · ")[0];
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM, yyyy", new Locale("ru"));
            return Optional.of(LocalDate.parse(datePart, formatter));
        } catch (DateTimeParseException e) {
            System.out.printf("✘ Ошибка при парсинге даты для курса \"%s\": %s%n", getTitle(), e.getMessage());
            return Optional.empty();
        } catch (Exception e) {
            System.out.printf("✘ Ошибка при парсинге даты для курса \"%s\": %s%n", getTitle(), e.getMessage());
            return Optional.empty();
        }
    }

    public void click() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // дождаться, чтобы элемент стал кликабельным
        wait.until(ExpectedConditions.elementToBeClickable(root));
        // скроллим в центр экрана
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center', inline:'center'});", root);
        try {
            root.click();
        } catch (ElementClickInterceptedException e) {
            System.out.println("Клик по элементу перехвачен, пробуем через JS");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", root);
        }
    }

    public String getInnerHtml() {
        return root.getAttribute("innerHTML");
    }

    public Document getJsoupDocument() {
        return Jsoup.parse(getInnerHtml());
    }

    public WebElement getRoot() {
        return root;
    }
}