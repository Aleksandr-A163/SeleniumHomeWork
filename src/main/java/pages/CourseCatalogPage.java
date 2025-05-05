package pages;

import com.google.inject.Inject;
import components.CookieBannerComponent;
import components.CourseListComponent;
import components.CourseCardComponent;
import org.openqa.selenium.WebDriver;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object для страницы каталога курсов.
 * Делегирует логику работы с баннером и списком карточек соответствующим компонентам.
 */
public class CourseCatalogPage {

    private static final String URL = "https://otus.ru/catalog/courses";

    private final WebDriver driver;
    private final CookieBannerComponent cookieBanner;
    private final CourseListComponent courseList;

    @Inject
    public CourseCatalogPage(WebDriver driver,
                              CookieBannerComponent cookieBanner,
                              CourseListComponent courseList) {
        this.driver = driver;
        this.cookieBanner = cookieBanner;
        this.courseList = courseList;
    }

    /** Открывает страницу каталога и ждёт загрузки карточек */
    public void open() {
        driver.get(URL);
        cookieBanner.acceptIfPresent();
        courseList.waitForReady();
    }

    /** Проверяет, что на странице отображены карточки курсов */
    public boolean isOpened() {
        courseList.waitForReady();
        return !courseList.getAllCards().isEmpty();
    }

    /** Возвращает все заголовки курсов */
    public List<String> getAllCourseTitles() {
        return courseList.getAllTitles();
    }

    /** Кликает по курсу с указанным названием */
    public void clickOnCourseByName(String name) {
        courseList.clickByName(name);
    }

    /** Возвращает список карточек, у которых есть дата старта */
    public List<CourseCardComponent> getAllCourseCardsWithDates() {
        return courseList.getCardsWithDates();
    }

    /** Находит самую раннюю дату старта среди карточек */
    public LocalDate getEarliestCourseDate() {
        return getAllCourseCardsWithDates().stream()
            .map(c -> c.tryGetStartDate().orElseThrow())
            .min(LocalDate::compareTo)
            .orElseThrow();
    }

    /** Находит самую позднюю дату старта среди карточек */
    public LocalDate getLatestCourseDate() {
        return getAllCourseCardsWithDates().stream()
            .map(c -> c.tryGetStartDate().orElseThrow())
            .max(LocalDate::compareTo)
            .orElseThrow();
    }

    /** Возвращает заголовки курсов, которые стартуют в заданную дату */
    public List<String> getCourseTitlesByDate(LocalDate date) {
        return getAllCourseCardsWithDates().stream()
            .filter(c -> c.tryGetStartDate().orElseThrow().equals(date))
            .map(CourseCardComponent::getTitle)
            .distinct()
            .collect(Collectors.toList());
    }

    /** Ждёт, пока карточки станут доступны в DOM */
    public void waitForCoursesToBeVisible() {
        courseList.waitForReady();
    }
}
