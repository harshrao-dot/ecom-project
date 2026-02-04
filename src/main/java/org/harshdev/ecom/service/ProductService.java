package org.harshdev.ecom.service;

import org.harshdev.ecom.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.harshdev.ecom.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository repository;

    public List<Product> getAllProducts(){
        return repository.findAll();
    }

    public Product addProduct(Product product){
        return repository.save(product);
    }

    public Product findById(String id){
        return repository.findById(id).orElse(null);
    }

    public void deleteProduct(String id){
        repository.deleteById(id);
    }

    public void updateById(String id, Product product){
        Product old = repository.findById(id).orElse(null);
        if(old != null){
            old.setDescription(product.getDescription());
            old.setName(product.getName());
            old.setPrice(product.getPrice());
            old.setStock(product.getStock());

            repository.save(old);
        }else{

        }
    }

    public void deleteAll(){
        repository.deleteAll();
    }

    public List<Product> getProductByPriceRanges(double min, double max){
        return repository.findByPriceBetween(min, max);
    }

    public List<Product> getByCategory(String category){
        return repository.findByCategoryIgnoreCase(category);
    }

    public List<Product> getAllProductsSortedByPrice(){
        return repository.findByOrderByPriceAsc();
    }
}
