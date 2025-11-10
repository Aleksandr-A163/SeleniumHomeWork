package ui_tests;

import com.google.inject.Inject;
import di.GuiceExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.CourseCatalogPage;
import pages.CoursePage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Проверка самого раннего и самого позднего курсов по дате начала (с использованием reduce)")
@Tag("data")
@ExtendWith(GuiceExtension.class)
public class CourseDateTest {

    @Inject
    private CourseCatalogPage catalog;

    @Inject
    private CoursePage course;

    @Test
    void shouldVerifyEarliestAndLatestCourseDates() {
        catalog.open();

        verifyCoursesForDate(catalog.getEarliestCourseDate());
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

            catalog.open();
        }
    }
}
