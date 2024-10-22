package com.example.demo;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "`order`")  // Escape the table name with backticks
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;
    private Double totalPrice;
    private String customerName;
    private String customerEmail;
    private String orderStatus = "pending"; 
    private LocalDateTime orderDate = LocalDateTime.now(); 

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;

    @PrePersist
    public void prePersist() {
        this.orderDate = LocalDateTime.now();
        this.orderStatus = this.orderStatus == null ? "pending" : this.orderStatus;
    }
}
