package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
public class Controller {
    
    private final Service service;

    @Autowired
    public Controller(Service service) {
        this.service = service;
    }

    // Endpoint to add a new product
    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product newProduct = service.addProduct(product);
        return ResponseEntity.ok(newProduct);
    }

    // Endpoint to get all products
    @GetMapping("/list")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = service.getAllProducts();
        return ResponseEntity.ok(products);
    }


    // ===============================================================
    // Endpoint to add a new order
    @PostMapping("/add-order")
    public ResponseEntity<Order> addOrder(@RequestBody Order order) {
        Order newOrder = service.addOrder(order);
        return ResponseEntity.ok(newOrder);
    }

    // Endpoint to get an order by id
    @GetMapping("/{orderId}/order")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        Optional<Order> order = service.getOrderById(orderId);
        return order.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create/payment")
    public ResponseEntity<Payment> createPayment(@RequestBody PaymentRequest paymentRequest) {

        // Retrieve the order by ID
        Order order = service.getOrderById(paymentRequest.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Create the payment
        Payment payment = service.createPayment(
                order,
                paymentRequest.getAmount(),
                paymentRequest.getPaymentMethod()
        );

        return ResponseEntity.ok(payment);
    }

}
