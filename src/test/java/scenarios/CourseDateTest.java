package scenarios;

import com.google.inject.Inject;
import di.GuiceExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import pages.CourseCatalogPage;
import pages.CoursePage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Проверка самого раннего и самого позднего курсов по дате начала")
@Tag("data")
@ExtendWith(GuiceExtension.class)
public class CourseDateTest {

    @Inject private WebDriver      driver;
    @Inject private CourseCatalogPage catalog;
    @Inject private CoursePage     course;

    @Test
    void shouldVerifyEarliestAndLatestCourseDates() {
        catalog.open();

        // Находим самую раннюю дату и проверяем все её курсы
        verifyCoursesForDate(catalog.getEarliestCourseDate());

        // Аналогично для самой поздней
        verifyCoursesForDate(catalog.getLatestCourseDate());
    }

    private void verifyCoursesForDate(LocalDate date) {
        for (String title : catalog.getCourseTitlesByDate(date)) {
            catalog.clickOnCourseByName(title);

            LocalDate actual = course.getCourseStartDateJsoup(date.getYear());
            assertEquals(
                date, actual,
                String.format("Курс '%s': ожидали %s, получили %s", title, date, actual)
            );

            driver.navigate().back();
            catalog.waitForCoursesToBeVisible();
        }
    }
}