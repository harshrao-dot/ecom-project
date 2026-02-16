package org.harshdev.ecom.controller;

import org.harshdev.ecom.Cart;
import org.harshdev.ecom.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(Authentication authentication, @RequestParam String productId, @RequestParam int quantity){
        String username = authentication.getName();

        Cart updatedCart = cartService.addItemToCart(username, productId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    @GetMapping("/view")
    public ResponseEntity<Cart> getCart(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(cartService.getCartByUserId(username));
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Cart> removeFromCart(
            Authentication authentication,
            @PathVariable String productId) {

        Cart updatedCart = cartService.removeItemFromCart(authentication.getName(), productId);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(Authentication authentication) {
        cartService.clearCart(authentication.getName());
        return ResponseEntity.ok("Cart is clear!");
    }

    @PutMapping("/update")
    public ResponseEntity<Cart> updateQuantity(
            Authentication authentication,
            @RequestParam String productId,
            @RequestParam int quantity) {

        Cart updatedCart = cartService.updateItemQuantity(authentication.getName(), productId, quantity);
        return ResponseEntity.ok(updatedCart);
    }
}
