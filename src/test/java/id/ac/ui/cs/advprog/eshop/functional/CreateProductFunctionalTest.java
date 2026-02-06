package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
public class CreateProductFunctionalTest {

    @LocalServerPort
    private int port;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + contextPath;
    }

    @Test
    void testCreateProduct(ChromeDriver driver) {
        driver.get(baseUrl + "/product/create");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nameInput")));

        String title = driver.getTitle();
        assertTrue(title.contains("Create New Product"), "Page title should contain 'Create New Product'");

        driver.findElement(By.id("nameInput")).sendKeys("Test Product Selenium");
        driver.findElement(By.id("quantityInput")).sendKeys("42");

        driver.findElement(By.cssSelector("button[type='submit']")).click();
        
        wait.until(ExpectedConditions.urlContains("/product/list"));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table tbody tr")));

        List<WebElement> rows = driver.findElements(By.cssSelector("table tbody tr"));
        boolean found = rows.stream()
                .anyMatch(r -> r.getText().contains("Test Product Selenium") && r.getText().contains("42"));

        assertTrue(found, "Created product should be visible on product list page");
    }
}
