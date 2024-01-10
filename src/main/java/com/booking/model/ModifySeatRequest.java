package com.booking.model;

import lombok.Data;

@Data
public class ModifySeatRequest {
    private String newSection;
    private Integer newSeatId;
    private String ticketId;
}
