package com.booking.entity;

import com.booking.model.User;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class TicketEntity {
    private String from;
    private String to;
    private User user;
    private double price;
    private String ticketId;
    private String sectionId;
}
