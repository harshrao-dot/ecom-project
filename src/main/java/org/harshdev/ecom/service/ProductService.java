package org.harshdev.ecom.service;

import org.harshdev.ecom.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.harshdev.ecom.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Product addProduct(Product product){
        return productRepository.save(product);
    }

    public Product findById(String id){
        return productRepository.findById(id).orElse(null);
    }

    public void deleteProduct(String id){
        productRepository.deleteById(id);
    }

    public void updateById(String id, Product product){
        Product old = productRepository.findById(id).orElse(null);
        if(old != null){
            old.setDescription(product.getDescription());
            old.setName(product.getName());
            old.setPrice(product.getPrice());
            old.setStock(product.getStock());

            productRepository.save(old);
        }
    }

    public void deleteAll(){
        productRepository.deleteAll();
    }

    public List<Product> getFilteredProducts(String name, String category, Double minPrice, Double maxPrice){

        // Gemini: Change - Default values set kari hain taaki query crash na kare
        String searchName = (name != null && !name.trim().isEmpty()) ? name : "null";
        String searchCat = (category != null && !category.trim().isEmpty()) ? category : "null";
        double min = (minPrice != null) ? minPrice : 0.0;
        double max = (maxPrice != null) ? maxPrice : Double.MAX_VALUE;

        // Gemini: Change - Ab ye ek hi method sab kuch filter kar dega (Name, Category, aur Price range)
        return productRepository.findByAdvancedFilter(searchName, searchCat, min, max);
    }
}