package org.harshdev.ecom.repository;

import org.harshdev.ecom.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    List<Product> findByPriceBetween(double min, double max);

    //category search
    List<Product> findByCategoryIgnoreCase(String category);

    //asc order(price low to high)
    List<Product> findByOrderByPriceAsc();

    List<Product> findByNameContainsIgnoreCase(String name);

    // Gemini: Change - Is query ko dynamic banaya hai taaki agar name/category null ho toh wo part skip ho jaye
    @Query("{ $and: [ " +
            "{ $or: [ { $expr: { $eq: ['?0', 'null'] } }, { 'name': { $regex: ?0, $options: 'i' } } ] }, " +
            "{ $or: [ { $expr: { $eq: ['?1', 'null'] } }, { 'category': { $regex: ?1, $options: 'i' } } ] }, " +
            "{ 'price' : { $gte: ?2, $lte: ?3 } } " +
            "] }")
    List<Product> findByAdvancedFilter(String name, String category, double minPrice, double maxPrice);
}