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

    /**
     * Получает дату начала курса со страницы, добавляя указанный год (т.к. он не отображается в HTML).
     *
     * @param fallbackYear Год, взятый с карточки курса
     * @return LocalDate с точной датой
     */
    public LocalDate getCourseStartDateJsoup(int fallbackYear) {
        String pageSource = driver.getPageSource();
        Document doc = Jsoup.parse(pageSource);

        // Ищем нужный <p> с датой, например: "24 апреля"
        Element dateParagraph = doc.selectFirst("p.doSDez");

        if (dateParagraph == null) {
            throw new IllegalStateException("Дата курса не найдена на странице курса.");
        }

        String dayMonthText = dateParagraph.text().trim(); // "24 апреля"
        String fullDateText = dayMonthText + ", " + fallbackYear; // "24 апреля, 2025"
        System.out.println(">>> Дата со страницы курса (Jsoup): " + fullDateText);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM, yyyy", new Locale("ru"));
        return LocalDate.parse(fullDateText, formatter);
    }
}