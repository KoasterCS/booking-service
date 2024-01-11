package com.booking.service.impl;

import java.util.ArrayList;

import java.util.Collections;

import java.util.List;

import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.booking.entity.SeatEntity;
import com.booking.entity.TicketEntity;
import com.booking.model.ModifySeatRequest;
import com.booking.model.PurchaseRequest;
import com.booking.model.Section;
import com.booking.model.SectionUser;
import com.booking.service.BookTicketService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookTicketServiceImpl implements BookTicketService {

    private final List<TicketEntity> tickets = new ArrayList<>();
    private final List<SeatEntity> seats = new ArrayList<>();
    private final List<Section> sections = Collections.synchronizedList(new ArrayList<>());
    public static final Integer MAX_SEATS = 60;

    public BookTicketServiceImpl() {
        // Initializing some in-memory values in place of database
        sections.add(Section.builder().sectionId("A").availableSeats(new int[MAX_SEATS]).seats(new ArrayList<>())
                .build());
        sections.add(Section.builder().sectionId("B").availableSeats(new int[MAX_SEATS]).seats(new ArrayList<>())
                .build());
    }

    @Override
    public SeatEntity bookTicketForUser(PurchaseRequest request) {

        Section sectionForBooking = getSection(request.getSection());
        SeatEntity seatEntity = null;

        if (ObjectUtils.isEmpty(sectionForBooking)) {
            log.error("Section is empty");
            return null;
        }

        synchronized (this) {
            String ticketId = UUID.randomUUID().toString();
            TicketEntity ticket = TicketEntity.builder().from(request.getFrom())
                    .to(request.getTo())
                    .user(request.getUser())
                    .price(request.getPrice())
                    .ticketId(ticketId)
                    .sectionId(request.getSection())
                    .build();
            tickets.add(ticket);
            List<SeatEntity> seatEntities = sectionForBooking.getSeats();
            Integer seatNumber = bookNextAvailableSeat(sectionForBooking);
            if(request.getSeatId()>0 && sectionForBooking.getAvailableSeats()[request.getSeatId()]==0){
                seatNumber = request.getSeatId();    
            }
           
            if (seatNumber != -1) {
                seatEntity = SeatEntity.builder().seatNumber(seatNumber)
                        .ticketDetails(ticket)
                        .isBooked(true)
                        .build();

                seatEntities.add(seatEntity);
                seats.add(seatEntity);
                return seatEntity;
            }

        }

        return seatEntity;
    }

    @Override
    public SeatEntity getTicketDetails(String ticketId) {
        return seats.stream().filter(seatFilter -> ticketId.equals(seatFilter.getTicketDetails().getTicketId()))
                .findAny()
                .orElse(null);
    }

    @Override
    public List<SectionUser> getSectionUsers(String sectionId) {
        Section section = getSection(sectionId);
        return section.getSeats().stream()
                .map(seatEntity -> SectionUser.builder()
                        .seatNumber(seatEntity.getSeatNumber())
                        .user(seatEntity.getTicketDetails().getUser())
                        .ticketId(seatEntity.getTicketDetails().getTicketId())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public Boolean deleteUser(String ticketId) {
        SeatEntity seatEntity = getTicketDetails(ticketId);
        Section section = getSection(seatEntity.getTicketDetails().getSectionId());
        section.getSeats().remove(seatEntity);
        section.getAvailableSeats()[seatEntity.getSeatNumber() - 1] = 0;
        return true;
    }

    @Override
    public SeatEntity modifySeatForUser(ModifySeatRequest modifySeatRequest) {
        SeatEntity seatEntity = getTicketDetails(modifySeatRequest.getTicketId());
        Section section = getSection(seatEntity.getTicketDetails().getSectionId());
        if (section.getSectionId().equals(modifySeatRequest.getNewSection())) {
            section.getSeats().remove(seatEntity);
            seats.remove(seatEntity);
            Integer seatNumber = modifySeatRequest.getNewSeatId();
            if (section.getAvailableSeats()[seatNumber] != 0) {
                seatNumber = bookNextAvailableSeat(section);
            }
            seatEntity.setSeatNumber(seatNumber);
            if (!modifySeatRequest.getNewSection().isEmpty()) {
                seatEntity.getTicketDetails().setSectionId(modifySeatRequest.getNewSection());
            }
            section.getAvailableSeats()[seatNumber] = 1;
            section.getSeats().add(seatEntity);
            seats.add(seatEntity);
            return seatEntity;
        } else {
            section.getSeats().remove(seatEntity);
            TicketEntity ticket = seatEntity.getTicketDetails();
            return bookTicketForUser(
                    PurchaseRequest.builder().from(ticket.getFrom()).to(ticket.getTo()).price(ticket.getPrice())
                            .section(modifySeatRequest.getNewSection()).user(ticket.getUser())
                            .seatId(modifySeatRequest.getNewSeatId())
                            .build());
        }
    }

    private Section getSection(String section) {
        // Would call database here but for now maintaining in-memory
        return sections.stream().filter(sectionFilter -> section.equals(sectionFilter.getSectionId()))
                .findAny()
                .orElse(null);
    }

    private int bookNextAvailableSeat(Section sectionForBooking) {
        int[] bookedSeats = sectionForBooking.getAvailableSeats();

        for (int i = 0; i < MAX_SEATS; i++) {
            if (bookedSeats[i] == 0) { // If seat is not booked (0 indicates available)
                bookedSeats[i] = 1; // Mark the seat as booked (1 indicates booked)
                return i + 1; // Seat numbers are assumed to start from 1
            }
        }

        // If no available seat is found
        return -1; // Or throw an exception indicating no available seats
    }


}
