package com.booking.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.booking.entity.SeatEntity;
import com.booking.model.ModifySeatRequest;
import com.booking.model.PurchaseRequest;
import com.booking.model.SectionUser;
import com.booking.model.User;
import com.booking.service.impl.BookTicketServiceImpl;

@ExtendWith(MockitoExtension.class)
public class BookTicketServiceImplTest {

    private BookTicketServiceImpl bookTicketServiceImpl;

    @BeforeEach
    void setUp() {
        bookTicketServiceImpl = new BookTicketServiceImpl();
    }

    @Test
    void testPurchaseTicket() {
        SeatEntity seatEntity = bookTicketServiceImpl.bookTicketForUser(getPurchaseRequest());
        assertEquals(20,seatEntity.getTicketDetails().getPrice());
        assertEquals(1,seatEntity.getSeatNumber());
    }

    @Test
    void testGetTicketDetails(){
        SeatEntity seatEntity = bookTicketServiceImpl.bookTicketForUser(getPurchaseRequest());
        SeatEntity requestSeatEntity = bookTicketServiceImpl.getTicketDetails(seatEntity.getTicketDetails().getTicketId());
        assertEquals(20,requestSeatEntity.getTicketDetails().getPrice());
        assertEquals(1,requestSeatEntity.getSeatNumber());
    }

    @Test
    void testGetSectionUser(){
        bookTicketServiceImpl.bookTicketForUser(getPurchaseRequest());
        List<SectionUser> sectionUser = bookTicketServiceImpl.getSectionUsers("A");
        assertTrue(!sectionUser.isEmpty());
    }

    @Test 
    void testDeleteUser(){
         SeatEntity seatEntity = bookTicketServiceImpl.bookTicketForUser(getPurchaseRequest());
         Boolean successfullDeletion = bookTicketServiceImpl.deleteUser(seatEntity.getTicketDetails().getTicketId());
         assertTrue(successfullDeletion);

    }

    @Test
    void modifySeatForUserDifferentSection(){
        SeatEntity seatEntity = bookTicketServiceImpl.bookTicketForUser(getPurchaseRequest());
        SeatEntity modifSeatEntity = bookTicketServiceImpl.modifySeatForUser(getModifySeatRequest(seatEntity.getTicketDetails().getTicketId(),"B"));
        assertEquals(2, modifSeatEntity.getSeatNumber());
        assertEquals("B", modifSeatEntity.getTicketDetails().getSectionId());
    }

    @Test
    void modifySeatForUserSameSection(){
        SeatEntity seatEntity = bookTicketServiceImpl.bookTicketForUser(getPurchaseRequest());
        SeatEntity modifSeatEntity = bookTicketServiceImpl.modifySeatForUser(getModifySeatRequest(seatEntity.getTicketDetails().getTicketId(),"A"));
        assertEquals(2, modifSeatEntity.getSeatNumber());
        assertEquals("A", modifSeatEntity.getTicketDetails().getSectionId());
    }

    private PurchaseRequest getPurchaseRequest() {
        return PurchaseRequest.builder().price(20).seatId(1).section("A").to("London").from("France")
                .user(User.builder().email("testEmail").firstName("firstname").lastName("testLastName").build())
                .build();
    }

    private ModifySeatRequest getModifySeatRequest(String ticketId,String sectionId){
        return ModifySeatRequest.builder().newSeatId(2).newSection(sectionId).ticketId(ticketId).build();
    }
}
