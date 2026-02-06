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
public class EditProductFunctionalTest {

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
    void testEditProduct(ChromeDriver driver) {
        String originalName = "ProductToEdit " + System.currentTimeMillis();
        String originalQuantity = "7";
        String updatedName = originalName + " Updated";
        String updatedQuantity = "77";

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        // Create product
        driver.get(baseUrl + "/product/create");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nameInput")));
        driver.findElement(By.id("nameInput")).sendKeys(originalName);
        driver.findElement(By.id("quantityInput")).sendKeys(originalQuantity);
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Wait for redirect to list
        wait.until(ExpectedConditions.urlContains("/product/list"));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table tbody tr")));

        // Find the created product row and click Edit
        List<WebElement> rows = driver.findElements(By.cssSelector("table tbody tr"));
        WebElement targetRow = rows.stream()
                .filter(r -> r.getText().contains(originalName) && r.getText().contains(originalQuantity))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Created product row not found"));

        targetRow.findElement(By.cssSelector("a.btn-outline-success")).click();

        // Wait for edit page
        wait.until(ExpectedConditions.urlContains("/product/edit"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nameInput")));

        // Update values
        WebElement nameInput = driver.findElement(By.id("nameInput"));
        nameInput.clear();
        nameInput.sendKeys(updatedName);

        WebElement quantityInput = driver.findElement(By.id("quantityInput"));
        quantityInput.clear();
        quantityInput.sendKeys(updatedQuantity);

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Wait for redirect to list and verify updated values
        wait.until(ExpectedConditions.urlContains("/product/list"));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table tbody tr")));

        List<WebElement> updatedRows = driver.findElements(By.cssSelector("table tbody tr"));
        boolean found = updatedRows.stream()
                .anyMatch(r -> r.getText().contains(updatedName) && r.getText().contains(updatedQuantity));

        assertTrue(found, "Edited product should be visible with updated values on product list page");
    }
}

