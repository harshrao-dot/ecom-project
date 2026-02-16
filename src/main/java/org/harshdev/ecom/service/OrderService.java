package org.harshdev.ecom.service;

import org.harshdev.ecom.*;
import org.harshdev.ecom.repository.OrderRepository;
import org.harshdev.ecom.repository.ProductRepository;
import org.harshdev.ecom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private EmailService emailService;

    @Transactional // Gemini: Agar stock update ho jaye par order save na ho, toh sab rollback ho jayega
    public Order checkout(String username, int addressIndex) {
        // 1. User fetch karo (Email aur Address ke liye)
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User nahi mila!"));

        // 2. Address validation
        if (user.getAddresses() == null || user.getAddresses().size() <= addressIndex) {
            throw new RuntimeException("Bhai, sahi address select kar ya naya add kar!");
        }
        Address deliveryAddress = user.getAddresses().get(addressIndex);

        // 3. User ka cart lao
        Cart cart = cartService.getCartByUserId(username);
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart khali hai, pehle kuch add kro!");
        }

        // 4. Stock update logic
        for (CartItem item : cart.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product nahi mila: " + item.getProductName()));

            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException(product.getName() + " out of stock ho gaya!");
            }

            // Actual stock minus
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        }

        //Create Order with Address
        Order order = new Order();
        order.setUserId(username);
        order.setItems(new ArrayList<>(cart.getItems()));
        order.setTotalAmount(cart.getTotalAmount());
        order.setStatus("PLACED");
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(deliveryAddress); //Order mein address save

        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(username);

        try {
            emailService.sendOrderConfirmation(user.getEmail(), savedOrder.getId());
        } catch (Exception e) {
            System.out.println("Email fail ho gaya par order done hai: " + e.getMessage());
        }

        return savedOrder;
    }

    public List<Order> getOrdersByUser(String userId){
        return orderRepository.findByUserId(userId);
    }

    public Order updateOrderStatus(String orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order nahi mila!"));

        order.setStatus(newStatus);

        if ("DELIVERED".equalsIgnoreCase(newStatus)) {
            order.setDeliveryDate(LocalDateTime.now());
        }

        return orderRepository.save(order);
    }

    //cancel order
    @Transactional //Taaki stock update aur status change dono ek saath ho
    public Order cancelOrder(String orderId, String userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order nahi mila!"));

        //Check ki ye usi user ka order hai ya nahi
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("Ye order aapka nahi hai!");
        }

        if ("DELIVERED".equalsIgnoreCase(order.getStatus())) {
            throw new RuntimeException("Jo order deliver ho gaya wo cancel nahi hoga!");
        }

        for (CartItem item : order.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product nahi mila: " + item.getProductName()));

            product.setStock(product.getStock() + item.getQuantity()); // Stock wapas + karo
            productRepository.save(product);
        }

        order.setStatus("CANCELLED");
        return orderRepository.save(order);
    }
}