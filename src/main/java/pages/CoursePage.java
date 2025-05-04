package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

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
}