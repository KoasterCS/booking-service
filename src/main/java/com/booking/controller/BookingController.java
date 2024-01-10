package com.booking.controller;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.booking.entity.SeatEntity;
import com.booking.model.ModifySeatRequest;
import com.booking.model.PurchaseRequest;
import com.booking.model.SectionUser;
import com.booking.service.BookTicketService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/tickets")
public class BookingController {

    private final BookTicketService bookTicketService;

    @PostMapping("/purchase")
    public SeatEntity purchaseTicket(@RequestBody PurchaseRequest request) {
        return bookTicketService.bookTicketForUser(request);
    }

    @GetMapping("/receipt/{ticketId}")
    public SeatEntity getReceipt(@PathVariable String ticketId) {
        return bookTicketService.getTicketDetails(ticketId);
    }

    @GetMapping("/users/{sectionId}")
    public List<SectionUser> getUserSeats(@PathVariable String sectionId) {
        return bookTicketService.getSectionUsers(sectionId);
    }

    @DeleteMapping("/remove/{ticketId}")
    public String removeUser(@PathVariable String ticketId) {
        Boolean isRemovalSuccessfull = bookTicketService.deleteUser(ticketId);
        if (Boolean.TRUE.equals(isRemovalSuccessfull))
            return "User removed from section ";
        else
            return "Failed to remove user";
    }

    @PutMapping("/modifySeat")
    public SeatEntity modifySeat(@RequestBody ModifySeatRequest request) {
        return bookTicketService.modifySeatForUser(request);
    }
}
