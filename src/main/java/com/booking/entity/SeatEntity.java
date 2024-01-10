package com.booking.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SeatEntity {
    private Integer seatNumber;
    private Boolean isBooked;
    private TicketEntity ticketDetails;
}
