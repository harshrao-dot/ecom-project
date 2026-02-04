package org.harshdev.ecom;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "order")
@Data
public class Order {
    @Id
    private String id;
    private String userId;
    private List<String> productIds;
    private double totalAmount;
    private String status;
}
