package scenarios;

import base.BaseTest;
import components.CourseCardComponent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.CourseCatalogPage;
import pages.CoursePage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Проверка самого раннего и самого позднего курсов по дате начала")
@Tag("data")
public class CourseDateTest extends BaseTest {

    @Test
    public void shouldVerifyEarliestAndLatestCourseDates() {
        CourseCatalogPage catalogPage = new CourseCatalogPage(driver);
        catalogPage.open();

        // 1. Собираем все курсы с датами
        List<CourseCardComponent> allCards = catalogPage.getAllCourseCardsWithDates();

        if (allCards.isEmpty()) {
            throw new RuntimeException("Не найдено ни одной карточки с датой старта");
        }

        // 2. Находим самую раннюю и самую позднюю дату
        LocalDate minDate = allCards.stream()
                .map(c -> c.tryGetStartDate().orElseThrow())
                .min(LocalDate::compareTo)
                .orElseThrow();
        LocalDate maxDate = allCards.stream()
                .map(c -> c.tryGetStartDate().orElseThrow())
                .max(LocalDate::compareTo)
                .orElseThrow();

        // 3. Находим названия курсов с этими датами
        List<String> earliestTitles = allCards.stream()
                .filter(c -> c.tryGetStartDate().orElseThrow().equals(minDate))
                .map(CourseCardComponent::getTitle)
                .toList();
        List<String> latestTitles = allCards.stream()
                .filter(c -> c.tryGetStartDate().orElseThrow().equals(maxDate))
                .map(CourseCardComponent::getTitle)
                .toList();

        // 4. Для каждого самого раннего курса: клик по названию, проверка даты, возврат назад
        for (String title : earliestTitles) {
            System.out.printf(">>> Проверяем ранний курс '%s' — %s%n", title, minDate);
            catalogPage.clickOnCourseByName(title);

            CoursePage page = new CoursePage(driver);
            LocalDate actual = page.getCourseStartDateJsoup(minDate.getYear());
            System.out.printf(">>> Дата со страницы курса: %s%n", actual);

            assertEquals(minDate, actual,
                String.format("Ранний курс '%s': ожидали %s, получили %s", title, minDate, actual));

            driver.navigate().back();
            catalogPage.waitForCoursesToBeVisible();
        }

        // 5. Для каждого самого позднего курса: аналогично
        for (String title : latestTitles) {
            System.out.printf(">>> Проверяем поздний курс '%s' — %s%n", title, maxDate);
            catalogPage.clickOnCourseByName(title);

            CoursePage page = new CoursePage(driver);
            LocalDate actual = page.getCourseStartDateJsoup(maxDate.getYear());
            System.out.printf(">>> Дата со страницы курса: %s%n", actual);

            assertEquals(maxDate, actual,
                String.format("Поздний курс '%s': ожидали %s, получили %s", title, maxDate, actual));

            driver.navigate().back();
            catalogPage.waitForCoursesToBeVisible();
        }
    }
}