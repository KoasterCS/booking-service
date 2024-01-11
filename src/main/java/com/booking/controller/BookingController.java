package com.booking.controller;

import java.util.List;

import static com.booking.util.ResponseBuilder.getErrorResponse;
import static com.booking.util.ResponseBuilder.getSuccessResponse;
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
    public Response<SeatEntity> purchaseTicket(@RequestBody PurchaseRequest request) {
        SeatEntity seatEntity = bookTicketService.bookTicketForUser(request);
        if(!ObjectUtils.isEmpty(seatEntity)){
            return getSuccessResponse(seatEntity,"Purchased ticket Successfully");
        }
        else{
            return getErrorResponse("There was an error processing your request, Please try again later");
        }
    }

    /**
     * @description Get receipt based on ticket id
     * @param ticketId
     * @return
     */
    @GetMapping("/receipt/{ticketId}")
    public Response<SeatEntity> getReceipt(@PathVariable String ticketId) {
        SeatEntity seatEntity = bookTicketService.getTicketDetails(ticketId);
         if(!ObjectUtils.isEmpty(seatEntity)){
            return getSuccessResponse(seatEntity,"Fetched ticket details Successfully");
        }
        else{
            return getErrorResponse("There was an error processing your request, Please try again later");
        }
    }

    /**
     * @description Get all users in a section
     * @param sectionId
     * @return
     */
    @GetMapping("/users/{sectionId}")
    public Response<List<SectionUser>> getUserSeats(@PathVariable String sectionId) {
        List<SectionUser> sectionUser = bookTicketService.getSectionUsers(sectionId);
        if(!ObjectUtils.isEmpty(sectionUser)){
            return getSuccessResponse(sectionUser,"Fetched users by section Successfully");
        }
        else{
            return getSuccessResponse("No Users were present in the requested section0");
        }
    }

    /**
     * @description Remove user based on ticketID
     * @param ticketId
     * @return
     */
    @DeleteMapping("/remove/{ticketId}")
    public Response<String> removeUser(@PathVariable String ticketId) {
        Boolean isRemovalSuccessfull = bookTicketService.deleteUser(ticketId);
        if (Boolean.TRUE.equals(isRemovalSuccessfull))
            return getSuccessResponse(null, "Removed ticket sucessfully");
        else
            return getErrorResponse("Failed to remove user, please try again later");
    }

    /**
     * @description Modify seat in the same or another section
     * @param request
     * @return
     */
    @PutMapping("/modifySeat")
    public Response<SeatEntity> modifySeat(@RequestBody ModifySeatRequest request) {
        SeatEntity seatEntity = bookTicketService.modifySeatForUser(request);
        if(!ObjectUtils.isEmpty(seatEntity)){
            return getSuccessResponse(seatEntity,"Modified ticket details Successfully");
        }
        else{
            return getErrorResponse("There was an error modifying request details, Please try again later");
        }
    }
}
