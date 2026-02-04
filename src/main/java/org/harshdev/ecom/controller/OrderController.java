package org.harshdev.ecom.controller;

import org.harshdev.ecom.Order;
import org.harshdev.ecom.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService service;

    @PostMapping("/place")
    public String PlaceOrder(@RequestBody Order order){
        return service.placeOrder(order);
    }

    @GetMapping("/user/{userId}")
    public List<Order> getUserOrders(@PathVariable String userId){
        return service.getOrdersByUser(userId);
    }
}
