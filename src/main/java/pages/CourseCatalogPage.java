package pages;

import com.google.inject.Inject;
import components.CourseListComponent;
import components.CourseCardComponent;
import org.openqa.selenium.WebDriver;

import java.time.LocalDate;
import java.util.List;

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

    public void open() {
        super.open();
        courseList.waitForReady();
    }


    public void clickOnCourseByName(String name) {
        courseList.clickByName(name);
    }

    public List<CourseCardComponent> getAllCourseCardsWithDates() {
        return courseList.getCardsWithDates();
    }

    public LocalDate getEarliestCourseDate() {
        return courseList.reduceToEarliestDate().orElseThrow();
    }

    public LocalDate getLatestCourseDate() {
        return courseList.reduceToLatestDate().orElseThrow();
    }

    public List<String> getCourseTitlesByDate(LocalDate date) {
        return courseList.getTitlesByDate(date);
    }

}
