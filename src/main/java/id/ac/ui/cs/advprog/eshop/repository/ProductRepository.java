package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
public class ProductRepository {
    private List<Product> productData = new ArrayList<>();

    public Product create(Product product) {
        productData.add(product);
        return product;
    }

    public Iterator<Product> findAll() {
        return productData.iterator();
    }

    public Product findById(String id) {
        for (Product product : productData) {
            if (product.getProductId().equals(id)) {
                return product;
            }
        }
        throw new NoSuchElementException("Product with ID " + id + " not found.");
    }

    public Product edit(Product updatedProduct) {
        String id = updatedProduct.getProductId();
        Product oldProduct = findById(id);
        oldProduct.setProductName(updatedProduct.getProductName());
        oldProduct.setProductQuantity(updatedProduct.getProductQuantity());
        return oldProduct;
    }

    public Product delete(Product product) {
        Product productToDelete = findById(product.getProductId());
        productData.remove(productToDelete);
        return productToDelete;
    }
}
