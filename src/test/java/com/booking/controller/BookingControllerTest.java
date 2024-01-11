package com.booking.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import com.booking.entity.SeatEntity;
import com.booking.model.ModifySeatRequest;
import com.booking.model.PurchaseRequest;
import com.booking.model.SectionUser;
import com.booking.service.BookTicketService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(BookingController.class)
@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {
    @Autowired
	private MockMvc mockMvc;

    @MockBean
	private BookTicketService bookTicketService;

    @InjectMocks
	private BookingController bookingController;

    public static final String PURCHASE_TICKET = "/api/tickets/purchase";
    public static final String RECIEPT = "/api/tickets/receipt/testTicketId";
    public static final String SECTION_USERS = "/api/tickets/users/testSectionID";
    public static final String REMOVE_USERS = "/api/tickets/remove/testTicketId";
    public static final String MODIFY_SEAT = "/api/tickets/modifySeat";

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
	void testPurchaseTicket() throws Exception {
        String payload = objectMapper.writeValueAsString(getPurchaseRequest());
        when(bookTicketService.bookTicketForUser(any())).thenReturn(getSeatEntity());
		
        MvcResult result = this.mockMvc.perform(post(PURCHASE_TICKET).content(payload).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		int responseStatus = result.getResponse().getStatus();
		assertEquals(200, responseStatus);
        assertNotNull(result);
	}    

    
	@Test
	void testGetTicketDetails() throws Exception {
        when(bookTicketService.getTicketDetails(anyString())).thenReturn(getSeatEntity());
		MvcResult result = this.mockMvc.perform(get(RECIEPT))
				.andExpect(status().isOk()).andReturn();
		int responseStatus = result.getResponse().getStatus();
		assertEquals(200, responseStatus);
	}

    @Test
	void testGetSectionUser() throws Exception {
        when(bookTicketService.getSectionUsers(anyString())).thenReturn(getSectionUser());
		MvcResult result = this.mockMvc.perform(get(SECTION_USERS))
				.andExpect(status().isOk()).andReturn();
		int responseStatus = result.getResponse().getStatus();
		assertEquals(200, responseStatus);
	}

    @Test
	void testDeleteUser() throws Exception {
        when(bookTicketService.deleteUser(anyString())).thenReturn(true);
		MvcResult result = this.mockMvc.perform(delete(REMOVE_USERS))
				.andExpect(status().isOk()).andReturn();
		int responseStatus = result.getResponse().getStatus();
		assertEquals(200, responseStatus);
	}

    @Test
	void testModifySeat() throws Exception {
        String payload = objectMapper.writeValueAsString(getModifySeatRequest());
        when(bookTicketService.modifySeatForUser(any())).thenReturn(getSeatEntity());
		MvcResult result = this.mockMvc.perform(put(MODIFY_SEAT).content(payload).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		int responseStatus = result.getResponse().getStatus();
		assertEquals(200, responseStatus);
	}

    private PurchaseRequest getPurchaseRequest(){
        return PurchaseRequest.builder().section("A").seatId(2).build();
    }

    private SeatEntity getSeatEntity(){
        return SeatEntity.builder().isBooked(true).seatNumber(1).build();
    }

    private List<SectionUser> getSectionUser(){
        return Arrays.asList(SectionUser.builder().seatNumber(1).ticketId("testTicketId").build());
    }

    private ModifySeatRequest getModifySeatRequest(){
        return ModifySeatRequest.builder().newSeatId(1).newSection("A").build();
    }
}
