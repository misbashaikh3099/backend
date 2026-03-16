package com.example.demo.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.CreateTicketRequest;
import com.example.demo.dto.TicketResponse;
import com.example.demo.model.Priority;
import com.example.demo.model.Ticket;
import com.example.demo.model.TicketHistory;
import com.example.demo.repository.TicketHistoryRepository;
import com.example.demo.repository.TicketRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketHistoryRepository historyRepository;

    public TicketResponse createTicket(CreateTicketRequest request) {

        Ticket ticket = new Ticket();

        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());

        ticket.setPriority(Priority.valueOf(request.getPriority()));
        ticket.setStatus("OPEN");
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setBuyerId(request.getBuyerId());

        ticket.setAttachments(request.getAttachments());

        Ticket savedTicket = ticketRepository.save(ticket);

        // SAVE HISTORY
        TicketHistory history = new TicketHistory();
        history.setTicketId(savedTicket.getId());
        history.setStatus("OPEN");
        /*history.setChangedBy(savedTicket.getBuyerId());*/
        history.setChangedBy(
                ticket.getVendorId() != null ? ticket.getVendorId() : ticket.getBuyerId()
        );
        history.setChangedAt(LocalDateTime.now());

        historyRepository.save(history);

        // RESPONSE
        TicketResponse response = new TicketResponse();
        response.setId(savedTicket.getId());
        response.setTitle(savedTicket.getTitle());
        response.setDescription(savedTicket.getDescription());
        response.setPriority(savedTicket.getPriority().name());
        response.setStatus(savedTicket.getStatus());
        response.setCreatedAt(savedTicket.getCreatedAt());

        return response;
    }

    // GET ALL TICKETS
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    // GET BUYER TICKETS
    public List<Ticket> getTicketsByBuyer(String buyerId) {
        return ticketRepository.findByBuyerId(buyerId);
    }

    // GET VENDOR TICKETS
    public List<Ticket> getTicketsByVendor(String vendorId) {
        return ticketRepository.findByVendorId(vendorId);
    }

    // ASSIGN VENDOR
    public Ticket assignVendor(String ticketId, String vendorId) {

        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();

        ticket.setVendorId(vendorId);
        ticket.setStatus("ASSIGNED");

        Ticket updatedTicket = ticketRepository.save(ticket);

        // SAVE HISTORY
        TicketHistory history = new TicketHistory();
        history.setTicketId(ticketId);
        history.setStatus("ASSIGNED");
        history.setChangedBy(vendorId);
        history.setChangedAt(LocalDateTime.now());

        historyRepository.save(history);

        return updatedTicket;
    }

    public Ticket updateStatus(String ticketId, String status) {

        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();

        ticket.setStatus(status);

        if (status.equals("RESOLVED")) {

            ticket.setResolvedAt(LocalDateTime.now());

            long hours = ChronoUnit.HOURS.between(
                    ticket.getCreatedAt(),
                    ticket.getResolvedAt()
            );

            ticket.setResolutionTimeHours(hours);
        }

        Ticket updatedTicket = ticketRepository.save(ticket);

        TicketHistory history = new TicketHistory();
        history.setTicketId(ticketId);
        history.setStatus(status);
        history.setChangedBy(ticket.getVendorId());
        history.setChangedAt(LocalDateTime.now());

        historyRepository.save(history);

        return updatedTicket;
    }

    public Ticket getTicketById(String id) {
        return ticketRepository.findById(id).orElseThrow();
    }

    public List<Ticket> getTicketsByPriority(String priority) {
        return ticketRepository.findByPriority(priority);
    }

    public Page<Ticket> getTickets(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return ticketRepository.findAll(pageable);
    }

    public List<Ticket> getTicketsByStatus(String status) {
        return ticketRepository.findByStatus(status);
    }

    public List<TicketHistory> getTicketHistory(String ticketId) {

        return historyRepository.findByTicketId(ticketId);
    }

    public List<Ticket> searchTickets(String keyword) {

        return ticketRepository.findByTitleContainingIgnoreCase(keyword);
    }
}
