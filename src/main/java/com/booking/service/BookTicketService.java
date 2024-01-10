package com.booking.service;

import java.util.List;

import com.booking.entity.SeatEntity;
import com.booking.model.ModifySeatRequest;
import com.booking.model.PurchaseRequest;
import com.booking.model.SectionUser;

public interface BookTicketService {
    public SeatEntity bookTicketForUser(PurchaseRequest request);

    public SeatEntity getTicketDetails(String ticketId);

    public List<SectionUser> getSectionUsers(String sectionId);  

    public Boolean deleteUser(String ticketId);    

    public SeatEntity modifySeatForUser(ModifySeatRequest modifySeatRequest);
}
