package org.harshdev.ecom.service;

import org.harshdev.ecom.Cart;
import org.harshdev.ecom.CartItem;
import org.harshdev.ecom.Product;
import org.harshdev.ecom.repository.CartRepository;
import org.harshdev.ecom.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    // 1. Item Add/Update Logic
    public Cart addItemToCart(String userId, String productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Stock Check: Cart mein add karte waqt sirf check kar rahe hain, minus nahi
        if (product.getStock() < quantity) {
            throw new RuntimeException("Stock kam hai! Available: " + product.getStock());
        }

        Cart cart = cartRepository.findByUserId(userId).orElse(new Cart(null, userId, new ArrayList<>(), 0.0));

        Optional<CartItem> existingItem = cart.getItems().stream().filter(item -> item.getProductId().equals(productId)).findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem(productId, product.getName(), quantity, product.getPrice());
            cart.getItems().add(newItem);
        }

        updateTotal(cart);
        return cartRepository.save(cart);
    }

    public Cart getCartByUserId(String userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart khali hai!"));
    }

    public Cart removeItemFromCart(String userId, String productId) {
        Cart cart = getCartByUserId(userId);
        cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        updateTotal(cart);
        return cartRepository.save(cart);
    }

    public void clearCart(String userId) {
        Cart cart = getCartByUserId(userId);
        cart.getItems().clear();
        cart.setTotalAmount(0.0);
        cartRepository.save(cart);
    }

    private void updateTotal(Cart cart) {
        double total = cart.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        cart.setTotalAmount(total);
    }

    public Cart updateItemQuantity(String userId, String productId, int newQuantity) {
        Cart cart = getCartByUserId(userId);

        // Agar quantity 0 ya usse kam hai, toh item hata do
        if (newQuantity <= 0) {
            return removeItemFromCart(userId, productId);
        }

        // Item dhoondo aur quantity update karo
        cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.setQuantity(newQuantity), // Agar mil gaya toh update
                        () -> { throw new RuntimeException("Item cart mein nahi hai!"); } // Agar nahi mila
                );

        updateTotal(cart);
        return cartRepository.save(cart);
    }
}