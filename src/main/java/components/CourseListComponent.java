package components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CourseListComponent {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By cards = By.cssSelector("a.sc-zzdkm7-0");

    @com.google.inject.Inject
    public CourseListComponent(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void waitForReady() {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(cards));
    }

    public List<CourseCardComponent> getAllCards() {
        List<WebElement> els = driver.findElements(cards);
        return els.stream()
                 .map(e -> new CourseCardComponent(driver, e))
                 .collect(Collectors.toList());
    }

    public void clickByName(String name) {
        waitForReady();

        List<CourseCardComponent> cards = getAllCards();
        if (cards.isEmpty()) {
            throw new IllegalStateException("❌ На странице не найдено ни одной карточки курса");
        }

        System.out.printf("🔎 Всего найдено курсов: %d%n", cards.size());
        cards.forEach(c -> System.out.println("— " + c.getTitle()));

        Optional<CourseCardComponent> course = cards.stream()
                .filter(c -> c.getTitle().trim().toLowerCase().contains(name.toLowerCase()))
                .findFirst();

        if (course.isEmpty()) {
            throw new IllegalArgumentException("❌ Курс не найден на странице: " + name);
        }

        course.get().click();
        System.out.println("✅ Курс успешно найден и кликнут: " + name);
    }

    public List<CourseCardComponent> getCardsWithDates() {
        return getAllCards().stream()
            .filter(c -> c.tryGetStartDate().isPresent())
            .collect(Collectors.toList());
    }

    public Optional<LocalDate> reduceToEarliestDate() {
        return getCardsWithDates().stream()
            .map(c -> c.tryGetStartDate().orElseThrow())
            .reduce((d1, d2) -> d1.isBefore(d2) ? d1 : d2);
    }

    public Optional<LocalDate> reduceToLatestDate() {
        return getCardsWithDates().stream()
            .map(c -> c.tryGetStartDate().orElseThrow())
            .reduce((d1, d2) -> d1.isAfter(d2) ? d1 : d2);
    }

    public List<String> getTitlesByDate(LocalDate date) {
        return getCardsWithDates().stream()
            .filter(c -> c.tryGetStartDate().orElseThrow().equals(date))
            .map(CourseCardComponent::getTitle)
            .distinct()
            .collect(Collectors.toList());
    }
}
