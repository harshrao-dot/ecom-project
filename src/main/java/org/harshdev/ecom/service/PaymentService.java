package org.harshdev.ecom.service;


import org.harshdev.ecom.Order;
import org.harshdev.ecom.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    @Autowired
    private OrderRepository orderRepository;

    public String processPayment(String orderId){
        boolean isSuccess = true;

        if(isSuccess){
            Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found!"));

            order.setStatus("Paid");
            orderRepository.save(order);
            return "SUCCESS";
        }

        return "FAILED";
    }
}
