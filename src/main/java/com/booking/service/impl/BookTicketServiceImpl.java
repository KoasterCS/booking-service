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

    BookTicketServiceImpl() {

        sections.add(Section.builder().sectionId("A").nextSeatToBeBooked(1).availableSeats(60).seats(new ArrayList<>())
                .build());
        sections.add(Section.builder().sectionId("B").nextSeatToBeBooked(1).availableSeats(60).seats(new ArrayList<>())
                .build());
    }

    public Section getSection(String section) {
        // Would call database here but for now maintaining in-memory
        return sections.stream().filter(sectionFilter -> section.equals(sectionFilter.getSectionId()))
                .findAny()
                .orElse(null);
    }

    public SeatEntity getSeatEntity(TicketEntity ticket) {
        return seats.stream().filter(seatFilter -> ticket.equals(seatFilter.getTicketDetails()))
                .findAny()
                .orElse(null);
    }

    public TicketEntity getTicket(String ticket) {
        // Would call database here but for now maintaining in-memory
        return tickets.stream().filter(ticketFilter -> ticket.equals(ticketFilter.getSectionId()))
                .findAny()
                .orElse(null);
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
            if (sectionForBooking.getAvailableSeats() != 0) {
                List<SeatEntity> seatEntities = sectionForBooking.getSeats();
                seatEntity = SeatEntity.builder().seatNumber(sectionForBooking.getNextSeatToBeBooked())
                        .ticketDetails(ticket).build();
                seatEntities.add(seatEntity);
                sectionForBooking.setAvailableSeats(sectionForBooking.getAvailableSeats() - 1);
                sectionForBooking.setNextSeatToBeBooked(sectionForBooking.getNextSeatToBeBooked() + 1);
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
        List<SectionUser> sectionUsers = new ArrayList<>();
        Section section = getSection(sectionId);
        sectionUsers = section.getSeats().stream()
                .map(seatEntity -> SectionUser.builder()
                        .sectionId(sectionId)
                        .user(seatEntity.getTicketDetails().getUser())
                        .build())
                .collect(Collectors.toList());
        return sectionUsers;
    }

}
