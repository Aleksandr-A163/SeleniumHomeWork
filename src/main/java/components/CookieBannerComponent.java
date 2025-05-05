package components;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CookieBannerComponent {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By okButton = By.xpath("//button[text()='OK']");

    @com.google.inject.Inject
    public CookieBannerComponent(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void acceptIfPresent() {
        try {
            WebElement btn = wait.until(
                ExpectedConditions.presenceOfElementLocated(okButton)
            );
            if (btn.isDisplayed()) {
                try {
                    btn.click();
                } catch (Exception ignored) {
                    ((JavascriptExecutor) driver)
                      .executeScript("arguments[0].click()", btn);
                }
            }
        } catch (TimeoutException ignored) { }
    }
}