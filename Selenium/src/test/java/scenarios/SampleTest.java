package scenarios;

import base.BaseTest;
import org.junit.jupiter.api.Test;

public class SampleTest extends BaseTest {

    @Test
    public void openOtus() {
        driver.get("https://otus.ru");
        System.out.println(driver.getTitle());
    }
}