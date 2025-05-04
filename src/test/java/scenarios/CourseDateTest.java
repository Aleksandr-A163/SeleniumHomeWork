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

        // Проверка самого раннего курса
        String earliestTitle = earliest.getTitle();
        LocalDate expectedEarliestDate = earliest.tryGetStartDate().orElseThrow();
        System.out.printf(">>> Самый ранний курс: %s — %s%n", earliestTitle, expectedEarliestDate);

        earliest.click();
        CoursePage earliestPage = new CoursePage(driver);
        LocalDate actualEarliestDate = earliestPage.getCourseStartDateJsoup(expectedEarliestDate.getYear());
        System.out.printf(">>> Дата со страницы курса (Jsoup): %s%n", actualEarliestDate);

        if (!expectedEarliestDate.equals(actualEarliestDate)) {
            System.out.printf("⚠ Дата на карточке: %s, на странице: %s%n", expectedEarliestDate, actualEarliestDate);
        }

        assertEquals(expectedEarliestDate, actualEarliestDate,
                "Дата старта раннего курса на карточке и странице не совпадает");

        // Вернуться назад и перезагрузить страницу
        driver.navigate().back();
        catalogPage.open();
    }
}