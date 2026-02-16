package org.harshdev.ecom.controller;

import org.harshdev.ecom.Order;
import org.harshdev.ecom.Payment;
import org.harshdev.ecom.service.OrderService;
import org.harshdev.ecom.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    //history of a user
    @GetMapping("/my-orders")
    public ResponseEntity<List<Order>> getUserOrders(Authentication authentication) {
        List<Order> orders = orderService.getOrdersByUser(authentication.getName());
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateStatus(@PathVariable String orderId, @RequestParam String status){
        return  ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(
            @PathVariable String orderId,
            Authentication authentication) {

        Order cancelledOrder = orderService.cancelOrder(orderId, authentication.getName());
        return ResponseEntity.ok(cancelledOrder);
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(Authentication auth, @RequestParam int addressIndex) {
        Order order = orderService.checkout(auth.getName(), addressIndex);

        String paymentResult = paymentService.processPayment(order.getId());

        if(paymentResult.equals("SUCCESS")) {
            return ResponseEntity.ok("Order aur Payment both are confirm! Order ID: " + order.getId());
        } else {
            return ResponseEntity.status(500).body("Payment declined!");
        }
    }


}