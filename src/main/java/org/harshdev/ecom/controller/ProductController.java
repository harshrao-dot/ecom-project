package org.harshdev.ecom.controller;

import org.harshdev.ecom.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.harshdev.ecom.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/all-products")
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    @PostMapping("/add-product")
    public Product addProduct(@RequestBody Product product){
        return productService.addProduct(product);
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable String id){
        return productService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id){
        productService.deleteProduct(id);
    }

    @PutMapping("/{id}")
    public void updateById(@PathVariable String id, @RequestBody Product product){
        productService.updateById(id, product);
    }

    @DeleteMapping("/deleteAll")
    public void deleteAll(){
        productService.deleteAll();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam(required = false) String name,
                                                        @RequestParam(required = false) String category,
                                                        @RequestParam(required = false) Double minPrice,
                                                        @RequestParam(required = false) Double maxPrice){

        return ResponseEntity.ok(productService.getFilteredProducts(name, category, minPrice, maxPrice));
    }
}