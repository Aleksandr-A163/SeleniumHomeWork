package components;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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
        root.click();
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