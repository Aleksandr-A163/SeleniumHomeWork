package pages;

import com.google.inject.Inject;
import components.CourseCardComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CourseCatalogPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final String url = "https://otus.ru/catalog/courses";
    private final By courseCardsSelector = By.cssSelector("a.sc-zzdkm7-0");
    private final By cookieBannerOkButton = By.xpath("//button[text()='OK']");

    @Inject
    public CourseCatalogPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open() {
        driver.get(url);
        acceptCookiesIfPresent();
        waitForCardsToLoad();
    }

    private void acceptCookiesIfPresent() {
        try {
            WebElement okButton = wait.until(
                ExpectedConditions.presenceOfElementLocated(cookieBannerOkButton)
            );
            if (okButton.isDisplayed()) {
                try {
                    okButton.click();
                } catch (Exception e) {
                    System.out.println("Клик по OK в cookie-баннере перехвачен, используем JS.");
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", okButton);
                }
            }
        } catch (TimeoutException e) {
            System.out.println("Cookie-баннер не появился.");
        }
    }

    private void waitForCardsToLoad() {
        // ждём, пока хотя бы одно появится в DOM
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(courseCardsSelector));
    }

    public boolean isOpened() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(courseCardsSelector));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private List<WebElement> getCourseCardElements() {
        return driver.findElements(courseCardsSelector);
    }

    public List<String> getAllCourseTitles() {
        return getCourseCardElements().stream()
            .map(element -> new CourseCardComponent(driver, element))
            .map(CourseCardComponent::getTitle)
            .collect(Collectors.toList());
    }

    public void clickOnCourseByName(String courseName) {
        getCourseCardElements().stream()
            .map(element -> new CourseCardComponent(driver, element))
            .filter(card -> card.getTitle().equalsIgnoreCase(courseName))
            .findFirst()
            .orElseThrow(() -> new RuntimeException(
                "Курс с названием '" + courseName + "' не найден"
            ))
            .click();
    }

    public List<CourseCardComponent> getAllCourseCardsWithDates() {
        return getCourseCardElements().stream()
            .map(element -> new CourseCardComponent(driver, element))
            .filter(card -> card.tryGetStartDate().isPresent())
            .collect(Collectors.toList());
    }

    public Optional<CourseCardComponent> getEarliestCourse() {
        return getAllCourseCardsWithDates().stream()
            .min(Comparator.comparing(card -> card.tryGetStartDate().get()));
    }

    public Optional<CourseCardComponent> getLatestCourse() {
        return getAllCourseCardsWithDates().stream()
            .max(Comparator.comparing(card -> card.tryGetStartDate().get()));
    }

    /**
     * Смена реализации: теперь ждём именно ПРИСУТСТВИЕ карточек в DOM,
     * а не их полную видимость на экране.
     */
    public void waitForCoursesToBeVisible() {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(courseCardsSelector));
    }
}