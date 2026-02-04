package org.harshdev.ecom.service;

import org.harshdev.ecom.Order;
import org.harshdev.ecom.Product;
import org.harshdev.ecom.User;
import org.harshdev.ecom.repository.OrderRepository;
import org.harshdev.ecom.repository.ProductRepository;
import org.harshdev.ecom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public String placeOrder(Order order){
        if(!userRepository.existsById(order.getUserId())){
            return "User nahi mila!!";
        }

        double total = 0;
        for(String pId : order.getProductIds()){
            Optional<Product> p = productRepository.findById(pId);
            if(p.isPresent()){
                Product product = p.get();
                if(product.getStock() > 0){
                    total += product.getPrice();
                    product.setStock(product.getStock() - 1);
                    productRepository.save(product);
                }else{
                    return "product " + product.getName() + "out of stock h!";
                }
            }else{
                return "product ID" + pId + "galat h!";
            }
        }

        order.setTotalAmount(total);
        order.setStatus("SUCCESS");
        repository.save(order);

        return "order place ho gaya! Total bill: " + total;
    }

    public List<Order> getOrdersByUser(String userId){
        return repository.findByUserId(userId);
    }
}
