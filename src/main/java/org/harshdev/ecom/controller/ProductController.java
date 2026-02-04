package org.harshdev.ecom.controller;


import org.harshdev.ecom.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.harshdev.ecom.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("/all-products")
    public List<Product> getAllProducts(){
        return service.getAllProducts();
    }

    @PostMapping("/add-product")
    public Product addProduct(@RequestBody Product product){
        return service.addProduct(product);
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable String id){
        return service.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id){
        service.deleteProduct(id);
    }

    @PutMapping("/{id}")
    public void updateById(@PathVariable String id, @RequestBody Product product){
        service.updateById(id, product);
    }

    @DeleteMapping("/deleteAll")
    public void deleteAll(){
        service.deleteAll();
    }

    @GetMapping("/filter-price")
    public List<Product> filterByPrice(@RequestParam double min, @RequestParam double max){
        return service.getProductByPriceRanges(min, max);
    }

    @GetMapping("/category/{category}")
    public List<Product> getByCategory(@PathVariable String category){
        return service.getByCategory(category);
    }

    @GetMapping("/sort/price-low")
    public List<Product> sortByPrice(){
        return service.getAllProductsSortedByPrice();
    }
}
