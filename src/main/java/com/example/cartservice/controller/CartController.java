package com.example.cartservice.controller;

import com.example.cartservice.pojo.Cart;
import com.example.cartservice.repository.JwtService;
import com.example.cartservice.service.CartService;
import com.example.cartservice.service.ErrorResponse;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/carts")
    public ResponseEntity<?> createCart(@RequestBody Cart cart , @RequestHeader("Authorization") String token) {
        String[] tokens = token.split(" ");
        if (tokens.length > 1) {
            Claims claims = jwtService.parseToken(tokens[1]);
            cart.setCustomer_id(claims.getSubject());
        }
        else {
            return ResponseEntity.status(404).body("ไม่พบ token");
        }
        try {
            return ResponseEntity.ok(cartService.createCart(cart));
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("ไม่สามารถเพิ่ม carts ใหม่ได้", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PutMapping("/carts/add")
    public ResponseEntity<?> updateCart(@RequestBody Cart cart, @RequestHeader("Authorization") String token) {
        String[] tokens = token.split(" ");
        String user = "";
        if (tokens.length > 1) {
            Claims claims = jwtService.parseToken(tokens[1]);
            user = claims.getSubject();
        }
        else {
            return ResponseEntity.status(404).body("ไม่พบ token");
        }
        try {
            return ResponseEntity.ok(cartService.updateCart(user,cart));
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("ไม่สามารถอัพเดต cart ได้", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PutMapping("/carts/del")
    public ResponseEntity<?> delCartProduct(@RequestBody Cart cart, @RequestHeader("Authorization") String token) {
        String[] tokens = token.split(" ");
        String user = "";
        if (tokens.length > 1) {
            Claims claims = jwtService.parseToken(tokens[1]);
            user = claims.getSubject();
        }
        else {
            return ResponseEntity.status(404).body("ไม่พบ token");
        }
        try {
            return ResponseEntity.ok(cartService.delCartProduct(user,cart));
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("ไม่สามารถอัพเดต cart ได้", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @DeleteMapping("/carts/{id}")
    public ResponseEntity<?> deleteCart(@PathVariable String id) {
        if(cartService.deleteCart(id)){
            return ResponseEntity.ok(true);
        }
        ErrorResponse errorResponse = new ErrorResponse("ไม่สามารถลบ cart ได้","ไอดีของ cart ไม่ถูกต้อง");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @PutMapping("/carts/clear")
    public ResponseEntity<?> delProduct(@RequestHeader("Authorization") String token) {
        String[] tokens = token.split(" ");
        String user = "";
        if (tokens.length > 1) {
            Claims claims = jwtService.parseToken(tokens[1]);
            user = claims.getSubject();
        }
        else {
            return ResponseEntity.status(404).body("ไม่พบ token");
        }
        try {
            return ResponseEntity.ok(cartService.clearProduct(user));
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("ไม่สามารถอัพเดต cart ได้", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/carts/{id}")
    public ResponseEntity<?> getCartById(@PathVariable String id) {
        if(cartService.getCartById(id).isEmpty() || cartService.getCartById(id) == null){
            ErrorResponse errorResponse = new ErrorResponse("ไม่พบ cart", "ไม่พบ cart ที่ต้องการ");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        return ResponseEntity.ok(cartService.getCartById(id));
    }

    @GetMapping("/carts")
    public ResponseEntity<?> getAllCarts() {
        return ResponseEntity.ok(cartService.getCart());
    }

    @GetMapping("/carts/me")
    public ResponseEntity<?> getCartByCustomerId(@RequestHeader("Authorization") String token) {
        String[] tokens = token.split(" ");
        String user_id = "";
        if (tokens.length > 1) {
            Claims claims = jwtService.parseToken(tokens[1]);
            user_id = claims.getSubject();
        }
        else {
            return ResponseEntity.status(404).body("ไม่พบ token");
        }
        try {
            return ResponseEntity.ok(cartService.getCartByCustomerId(user_id));
        }
        catch (Exception e){
            ErrorResponse errorResponse = new ErrorResponse("เกิดข้อผิดพลาด", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/carts/sum")
    public ResponseEntity<?> SumPrice(@RequestHeader("Authorization") String token) {
        String[] tokens = token.split(" ");
        String user_id = "";
        if (tokens.length > 1) {
            Claims claims = jwtService.parseToken(tokens[1]);
            user_id = claims.getSubject();
        }
        else {
            return ResponseEntity.status(404).body("ไม่พบ token");
        }
        try {
            return ResponseEntity.ok(cartService.calculateTotalPrice(user_id));
        }
        catch (Exception e){
            ErrorResponse errorResponse = new ErrorResponse("เกิดข้อผิดพลาด", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/carts/order")
    public ResponseEntity<?> CreatOrder(@RequestHeader("Authorization") String token) {
        String[] tokens = token.split(" ");
        String user_id = "";
        if (tokens.length > 1) {
            Claims claims = jwtService.parseToken(tokens[1]);
            user_id = claims.getSubject();
        }
        else {
            return ResponseEntity.status(404).body("ไม่พบ token");
        }
        try {
            return ResponseEntity.ok(cartService.createOrder(user_id, tokens[1]));
        }
        catch (Exception e){
            ErrorResponse errorResponse = new ErrorResponse("เกิดข้อผิดพลาด", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }



}
