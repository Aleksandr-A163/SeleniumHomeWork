package scenarios;

import base.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.CourseCatalogPage;
import pages.CoursePage;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Проверка перехода на случайную страницу курса")
public class CourseSearchTest extends BaseTest {

    @Test
    public void shouldFindAndOpenRandomCourse() {
        // Заранее заданный список курсов для выбора
        List<String> courseNames = List.of(
                "Microservice Architecture",
                "Highload Architect",
                "Unreal Engine Game Developer. Basic"
        );

        // Выбор случайного названия курса из списка
        String courseName = courseNames.get(new Random().nextInt(courseNames.size()));
        System.out.println("Выбран курс: " + courseName);

        // Открытие страницы каталога курсов
        CourseCatalogPage catalogPage = new CourseCatalogPage(driver);
        catalogPage.open();

        // Печать всех заголовков курсов, найденных на странице
        System.out.println("Все найденные курсы:");
        catalogPage.getAllCourseTitles().forEach(System.out::println);

        // Проверки: страница каталога открыта, курс из списка присутствует на странице
        assertAll("Проверка каталога курсов",
                () -> assertTrue(catalogPage.isOpened(), "Каталог не открылся"),
                () -> assertTrue(
                        catalogPage.getAllCourseTitles().contains(courseName),
                        "Курс не найден в списке: " + courseName
                )
        );

        // Клик по карточке курса с нужным названием
        catalogPage.clickOnCourseByName(courseName);

        // Открытие страницы курса и проверка соответствия заголовка
        CoursePage coursePage = new CoursePage(driver);
        assertTrue(coursePage.isCorrectCourseOpened(courseName),
                "Открыт неверный курс. Ожидался: " + courseName);
    }
}