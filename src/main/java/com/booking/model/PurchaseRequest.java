package com.booking.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PurchaseRequest {
    @NotEmpty(message = "source may not be null")
    private String from;
    @NotEmpty(message = "destination may not be null")
    private String to;
    @NotEmpty(message = "section may not be null")
    private String section;
    private User user;
    private double price;
}
