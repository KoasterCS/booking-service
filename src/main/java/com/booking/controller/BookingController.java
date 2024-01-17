package com.booking.controller;

import java.util.List;

import static com.booking.util.ResponseBuilder.getErrorResponse;
import static com.booking.util.ResponseBuilder.getSuccessResponse;
import static com.booking.util.ResponseBuilder.getBadRequestResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
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
import com.booking.model.Response;
import com.booking.model.SectionUser;
import com.booking.service.BookTicketService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/tickets")
public class BookingController {

    private final BookTicketService bookTicketService;

    /**
     * @description Purchase request for Seat
     * @param request
     * @return
     */
    @PostMapping("/purchase")
    public ResponseEntity<Response<SeatEntity>> purchaseTicket(@RequestBody PurchaseRequest request) {
        Response<SeatEntity> response = null;
        SeatEntity seatEntity = bookTicketService.bookTicketForUser(request);
        if (!ObjectUtils.isEmpty(seatEntity)) {
            response = getSuccessResponse(seatEntity, "Purchased ticket Successfully");
        } else {
            response = getErrorResponse("There was an error processing your request, Please try again later");
        }

        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * @description Get receipt based on ticket id
     * @param ticketId
     * @return
     */
    @GetMapping("/receipt/{ticketId}")
    public ResponseEntity<Response<SeatEntity>> getReceipt(@PathVariable String ticketId) {
        Response<SeatEntity> response = null;
        SeatEntity seatEntity = bookTicketService.getTicketDetails(ticketId);
        if (!ObjectUtils.isEmpty(seatEntity)) {
            response = getSuccessResponse(seatEntity, "Fetched ticket details Successfully");
        } else {
            response = getSuccessResponse("There was no such ticket found");
        }
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * @description Get all users in a section
     * @param sectionId
     * @return
     */
    @GetMapping("/users/{sectionId}")
    public ResponseEntity<Response<List<SectionUser>>> getUserSeats(@PathVariable String sectionId) {
        Response<List<SectionUser>> response = null;
        List<SectionUser> sectionUser = bookTicketService.getSectionUsers(sectionId);
        if (!ObjectUtils.isEmpty(sectionUser)) {
            response = getSuccessResponse(sectionUser, "Fetched users by section Successfully");
        } else {
            response = getSuccessResponse("No Users were present in the requested section");
        }
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * @description Remove user based on ticketID
     * @param ticketId
     * @return
     */
    @DeleteMapping("/remove/{ticketId}")
    public ResponseEntity<Response<String>> removeUser(@PathVariable String ticketId) {
        Response<String> response = null;
        Boolean isRemovalSuccessfull = bookTicketService.deleteUser(ticketId);
        if (Boolean.TRUE.equals(isRemovalSuccessfull))
            response = getSuccessResponse(null, "Removed ticket sucessfully");
        else
            response = getErrorResponse("Failed to remove user, please try again later");
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * @description Modify seat in the same or another section
     * @param request
     * @return
     */
    @PutMapping("/modifySeat")
    public ResponseEntity<Response<SeatEntity>> modifySeat(@RequestBody ModifySeatRequest request) {
        Response<SeatEntity> response = null;
        SeatEntity seatEntity = bookTicketService.modifySeatForUser(request);
        if (!ObjectUtils.isEmpty(seatEntity)) {
            response = getSuccessResponse(seatEntity, "Modified ticket details Successfully");
        } else {
            response = getErrorResponse("There was an error modifying request details, Please try again later");
        }
        return new ResponseEntity<>(response, response.getStatus());
    }
}
