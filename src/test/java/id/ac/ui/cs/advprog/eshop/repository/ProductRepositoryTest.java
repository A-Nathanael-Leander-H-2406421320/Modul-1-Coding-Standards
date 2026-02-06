package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @InjectMocks
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateAndFind() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOneProduct() {
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460g-8868-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("a0f9de46-90b1-437d-a0bf-d0821dde9096");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);
        productRepository.create(product2);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product1.getProductId(), savedProduct.getProductId());
        savedProduct = productIterator.next();
        assertEquals(product2.getProductId(), savedProduct.getProductId());
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindProductByIdSuccess() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Product foundProduct = productRepository.findById("eb558e9f-1c39-460e-8860-71af6af63bd6");
        assertNotNull(foundProduct);
        assertEquals(product.getProductId(), foundProduct.getProductId());
        assertEquals(product.getProductName(), foundProduct.getProductName());
        assertEquals(product.getProductQuantity(), foundProduct.getProductQuantity());
    }

    @Test
    void testFindProductByIdFail() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Exception exception = assertThrows(Exception.class, () -> {
            productRepository.findById("a0f9de46-90b1-437d-a0bf-d0821dde9096");
        });
        assertEquals(NoSuchElementException.class, exception.getClass());
        assertEquals("Product with ID a0f9de46-90b1-437d-a0bf-d0821dde9096 not found.", exception.getMessage());
    }

    @Test
    void testFindProductByIdEmptyRepo() {
        Exception exception = assertThrows(Exception.class, () -> {
            productRepository.findById("a0f9de46-90b1-437d-a0bf-d0821dde9096");
        });
        assertEquals(NoSuchElementException.class, exception.getClass());
        assertEquals("Product with ID a0f9de46-90b1-437d-a0bf-d0821dde9096 not found.", exception.getMessage());
    }

    @Test
    void testEditProductSuccess() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Product updatedProduct = new Product();
        updatedProduct.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        updatedProduct.setProductName("Sampo Cap Budi");
        updatedProduct.setProductQuantity(150);

        Product editedProduct = productRepository.edit(updatedProduct);
        assertEquals(updatedProduct.getProductId(), editedProduct.getProductId());
        assertEquals(updatedProduct.getProductName(), editedProduct.getProductName());
        assertEquals(updatedProduct.getProductQuantity(), editedProduct.getProductQuantity());
    }

    @Test
    void testEditProductFail() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Product updatedProduct = new Product();
        updatedProduct.setProductId("a0f9de46-90b1-437d-a0bf-d0821dde9096");
        updatedProduct.setProductName("Sampo Cap Budi");
        updatedProduct.setProductQuantity(150);

        Exception exception = assertThrows(Exception.class, () -> {
            productRepository.edit(updatedProduct);
        });
        assertEquals(NoSuchElementException.class, exception.getClass());
        assertEquals("Product with ID a0f9de46-90b1-437d-a0bf-d0821dde9096 not found.", exception.getMessage());
    }

    @Test
    void testDeleteProductSuccess() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Product deletedProduct = productRepository.delete(product);
        assertEquals(product.getProductId(), deletedProduct.getProductId());
        assertEquals(product.getProductName(), deletedProduct.getProductName());
        assertEquals(product.getProductQuantity(), deletedProduct.getProductQuantity());

        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testDeleteProductFail() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Product productToDelete = new Product();
        productToDelete.setProductId("a0f9de46-90b1-437d-a0bf-d0821dde9096");
        productToDelete.setProductName("Sampo Cap Usep");
        productToDelete.setProductQuantity(50);

        Exception exception = assertThrows(Exception.class, () -> {
            productRepository.delete(productToDelete);
        });
        assertEquals(NoSuchElementException.class, exception.getClass());
        assertEquals("Product with ID a0f9de46-90b1-437d-a0bf-d0821dde9096 not found.", exception.getMessage());
    }
}
