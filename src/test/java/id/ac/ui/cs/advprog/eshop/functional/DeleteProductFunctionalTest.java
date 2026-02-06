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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
public class DeleteProductFunctionalTest {

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
    void testDeleteProduct(ChromeDriver driver) {
        String productName = "ProductToDelete " + System.currentTimeMillis();
        String productQuantity = "9";

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        // Create product
        driver.get(baseUrl + "/product/create");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nameInput")));
        driver.findElement(By.id("nameInput")).sendKeys(productName);
        driver.findElement(By.id("quantityInput")).sendKeys(productQuantity);
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Wait for redirect to list
        wait.until(ExpectedConditions.urlContains("/product/list"));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table tbody tr")));

        // Find the created product row and click Delete
        List<WebElement> rows = driver.findElements(By.cssSelector("table tbody tr"));
        WebElement targetRow = rows.stream()
                .filter(r -> r.getText().contains(productName) && r.getText().contains(productQuantity))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Created product row not found"));

        targetRow.findElement(By.cssSelector("a.btn-outline-danger")).click();

        // Wait for delete confirmation page
        wait.until(ExpectedConditions.urlContains("/product/delete"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button[type='submit']")));

        // Confirm delete
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Wait for redirect to list and verify updated table
        wait.until(ExpectedConditions.urlContains("/product/list"));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table tbody")));

        List<WebElement> updatedRows = driver.findElements(By.cssSelector("table tbody tr"));
        boolean stillPresent = updatedRows.stream()
                .anyMatch(r -> r.getText().contains(productName) && r.getText().contains(productQuantity));

        assertFalse(stillPresent, "Deleted product should no longer appear on the product list page");
    }
}
