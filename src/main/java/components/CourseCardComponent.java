package components;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CourseCardComponent {

    private final WebDriver driver;
    private final WebElement root;

    private final By titleSelector = By.cssSelector("h6 > div");

    public CourseCardComponent(WebDriver driver, WebElement root) {
        this.driver = driver;
        this.root = root;
    }

    public String getTitle() {
        return root.findElement(titleSelector).getText().trim();
    }

    public void click() {
        try {
            root.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            System.out.println("Клик по курсу перехвачен другим элементом, используем JavaScript.");
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", root);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", root);
        }
    }

}