package com.booking.model;

import java.beans.Transient;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PurchaseRequest {
    @NotEmpty(message = "source may not be null")
    private String from;
    @NotEmpty(message = "destination may not be null")
    private String to;
    @NotEmpty(message = "section may not be null")
    private String section;
    private Integer seatId;
    private User user;
    private double price;
    private String ticketId;
}
