package components;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

public class CourseCardComponent {
    private final WebElement root;
    private final WebDriver driver;

    private final By titleSelector = By.cssSelector("h6 div");
    private final By dateTextSelector = By.cssSelector("div[class*='sc-157icee-1'] > div");

    public CourseCardComponent(WebDriver driver, WebElement root) {
        this.driver = driver;
        this.root = root;
    }

    public String getTitle() {
        return root.findElement(titleSelector).getText().trim();
    }

    public Optional<LocalDate> tryGetStartDate() {
        try {

            String fullText = root.findElement(dateTextSelector).getText().trim();
            String datePart = fullText.split(" · ")[0].trim();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM, yyyy", new Locale("ru"));
            return Optional.of(LocalDate.parse(datePart, formatter));

        } catch (Exception e) {
            System.err.printf("✘ Не удалось получить дату курса \"%s\": %s%n", getTitle(), e.getMessage());
            return Optional.empty();
        }
    }


    public void click() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(root));

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'center'});", root);

        try {
            Thread.sleep(100); // задержка перед кликом
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        try {
            root.click();
        } catch (ElementClickInterceptedException e) {
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