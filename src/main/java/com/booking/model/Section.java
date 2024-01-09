package com.booking.model;

import java.util.List;

import com.booking.entity.SeatEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Section {
    private String sectionId;
    private Integer availableSeats;
    private Integer nextSeatToBeBooked;
    private List<SeatEntity> seats;
}
