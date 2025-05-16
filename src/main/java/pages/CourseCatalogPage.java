package pages;

import com.google.inject.Inject;
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
public class CourseCatalogPage extends BasePage {

    private final CourseListComponent courseList;

    @Inject
    public CourseCatalogPage(WebDriver driver,
                             CourseListComponent courseList) {
        super(driver);
        this.courseList = courseList;
    }

    @Override
    protected String getPath() {
        return "/catalog/courses";
    }

    /** Открывает страницу каталога и ждёт загрузки карточек */
    public void open() {
        super.open();
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