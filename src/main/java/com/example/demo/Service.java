package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class Service {

    private final ProductRepository productRepository;



    // Method to add a new product
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    // Method to get the list of all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


    private final OrderRepository orderRepository;

    @Autowired
    public Service(ProductRepository productRepository, OrderRepository orderRepository, PaymentRepository paymentRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    private final PaymentRepository paymentRepository;
    
    // Add a new order
    public Order addOrder(Order order) {
        return orderRepository.save(order);
    }

    // Get order by id
    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    @Transactional
    public Payment createPayment(Order order, Double amount, String paymentMethod) {
        // Kiểm tra nếu số tiền thanh toán không khớp với tổng tiền của đơn hàng
        if (!order.getTotalPrice().equals(amount)) {
            throw new IllegalArgumentException("Payment amount does not match the order total!");
        }

        // Kiểm tra nếu đơn hàng đã được thanh toán
        if ("ORDER SUCCESS".equals(order.getOrderStatus())) {
            throw new IllegalArgumentException("Order has already been paid!");
        }

        Payment payment = Payment.builder()
                .order(order)
                .amount(amount)
                .paymentMethod(paymentMethod)
                .paymentDate(LocalDateTime.now())
                .build();

        try {
            // Simulate payment processing logic
            // Here we assume the payment is always successful
            payment.setPaymentStatus("success");

            // Update the associated order status
            order.setOrderStatus("ORDER SUCCESS");
            orderRepository.save(order);

        } catch (Exception e) {
            // If an error occurs, mark payment as failed
            payment.setPaymentStatus("failed");

            // Update the order status as canceled
            order.setOrderStatus("CANCEL");
            orderRepository.save(order);
        }

        return paymentRepository.save(payment);
    }

}
