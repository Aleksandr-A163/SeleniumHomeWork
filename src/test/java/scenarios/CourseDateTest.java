package scenarios;

import base.BaseTest;
import components.CourseCardComponent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.CourseCatalogPage;
import pages.CoursePage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Проверка самого раннего и самого позднего курсов по дате начала")
@Tag("data")
public class CourseDateTest extends BaseTest {

    @Test
    public void shouldVerifyEarliestAndLatestCourseDates() {
        CourseCatalogPage catalogPage = new CourseCatalogPage(driver);
        catalogPage.open();

        // Получение курсов с самой ранней и самой поздней датой
        CourseCardComponent earliest = catalogPage.getEarliestCourse()
                .orElseThrow(() -> new RuntimeException("Не удалось найти курс с самой ранней датой"));
        CourseCardComponent latest = catalogPage.getLatestCourse()
                .orElseThrow(() -> new RuntimeException("Не удалось найти курс с самой поздней датой"));

        // Проверка раннего курса
        String earliestTitle = earliest.getTitle();
        LocalDate expectedEarliestDate = earliest.tryGetStartDate().orElseThrow();
        System.out.println(">>> Самый ранний курс: " + earliestTitle + " — " + expectedEarliestDate);

        earliest.click();
        CoursePage earliestPage = new CoursePage(driver);
        LocalDate actualEarliestDate = earliestPage.getCourseStartDateJsoup();
        assertEquals(expectedEarliestDate, actualEarliestDate,
                "Дата старта раннего курса на карточке и странице не совпадает");

        // Вернуться назад
        driver.navigate().back();
        catalogPage.open(); // повторная загрузка страницы

        // Проверка позднего курса
        CourseCardComponent latestUpdated = catalogPage.getAllCourseCardsWithDates().stream()
                .filter(c -> c.getTitle().equals(latest.getTitle()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Не удалось повторно найти курс с самой поздней датой"));

        String latestTitle = latestUpdated.getTitle();
        LocalDate expectedLatestDate = latestUpdated.tryGetStartDate().orElseThrow();
        System.out.println(">>> Самый поздний курс: " + latestTitle + " — " + expectedLatestDate);

        latestUpdated.click();
        CoursePage latestPage = new CoursePage(driver);
        LocalDate actualLatestDate = latestPage.getCourseStartDateJsoup();
        assertEquals(expectedLatestDate, actualLatestDate,
                "Дата старта позднего курса на карточке и странице не совпадает");
    }
}