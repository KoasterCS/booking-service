package com.booking.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionUser {
    private Integer seatNumber;
    private String ticketId;
    private User user;
}
