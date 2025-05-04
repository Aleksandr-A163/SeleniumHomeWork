package pages;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CoursePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Актуальный селектор заголовка курса
    private final By titleSelector = By.cssSelector("h1.diGrSa");

    public CoursePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public String getCourseTitle() {
        WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(titleSelector));
        return titleElement.getText().trim();
    }

    public boolean isCorrectCourseOpened(String expectedTitle) {
        String actualTitle = getCourseTitle();
        return actualTitle.equalsIgnoreCase(expectedTitle);
    }

        // Новый метод для получения даты старта курса через Jsoup
    public LocalDate getCourseStartDateJsoup() {
        String pageSource = driver.getPageSource();
        Document doc = Jsoup.parse(pageSource);

        Element dateBlock = doc.selectFirst("div.sc-hrqzy3-1.jEGzDf:matchesOwn(\\d{1,2} [а-я]+, \\d{4})");

        if (dateBlock == null) {
            throw new IllegalStateException("Дата старта курса не найдена на странице курса.");
        }

        String rawDate = dateBlock.text().split("·")[0].trim(); // "24 апреля, 2025"
        System.out.println(">>> Дата со страницы курса (Jsoup): " + rawDate);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM, yyyy", new Locale("ru"));
        return LocalDate.parse(rawDate, formatter);
    }
}