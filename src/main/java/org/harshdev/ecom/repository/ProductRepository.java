package org.harshdev.ecom.repository;

import org.harshdev.ecom.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    List<Product> findByPriceBetween(double min, double max);

    //category search
    List<Product> findByCategoryIgnoreCase(String category);

    //asc order(price low to high)
    List<Product> findByOrderByPriceAsc();
}
