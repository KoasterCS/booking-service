package com.booking.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModifySeatRequest {
    private String newSection;
    private Integer newSeatId;
    private String ticketId;
}
