package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CreateTicketRequest;
import com.example.demo.dto.TicketResponse;
import com.example.demo.model.Ticket;
import com.example.demo.model.TicketHistory;
import com.example.demo.service.ReportService;
import com.example.demo.service.TicketService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TicketController {

    private final TicketService ticketService;
    private final ReportService reportService;

    @PostMapping
    public TicketResponse createTicket(@RequestBody CreateTicketRequest request) {
        return ticketService.createTicket(request);
    }

    @GetMapping("/buyer/{buyerId}")
    public List<Ticket> getBuyerTickets(@PathVariable String buyerId) {
        return ticketService.getTicketsByBuyer(buyerId);
    }

    @GetMapping
    public List<Ticket> getAllTickets() {
        return ticketService.getAllTickets();
    }

    // ASSIGN VENDOR
    @PutMapping("/{ticketId}/assign/{vendorId}")
    public Ticket assignVendor(
            @PathVariable String ticketId,
            @PathVariable String vendorId) {

        return ticketService.assignVendor(ticketId, vendorId);
    }

    // GET VENDOR TICKETS
    @GetMapping("/vendor/{vendorId}")
    public List<Ticket> getVendorTickets(@PathVariable String vendorId) {
        return ticketService.getTicketsByVendor(vendorId);
    }

    @PutMapping("/{ticketId}/status/{status}")
    public Ticket updateStatus(
            @PathVariable String ticketId,
            @PathVariable String status) {

        return ticketService.updateStatus(ticketId, status);
    }

    @GetMapping("/{id}")
    public Ticket getTicket(@PathVariable String id) {
        return ticketService.getTicketById(id);
    }

    @GetMapping("/priority/{priority}")
    public List<Ticket> getTicketsByPriority(@PathVariable String priority) {
        return ticketService.getTicketsByPriority(priority);
    }

    @GetMapping("/priority")
    public Map<String, Long> getPriorityStats() {
        return reportService.getPriorityStats();
    }

    @GetMapping("/page")
    public Page<Ticket> getTickets(
            @RequestParam int page,
            @RequestParam int size
    ) {
        return ticketService.getTickets(page, size);
    }

    @GetMapping("/status/{status}")
    public List<Ticket> getTicketsByStatus(@PathVariable String status) {
        return ticketService.getTicketsByStatus(status);
    }

    @GetMapping("/{ticketId}/history")
    public List<TicketHistory> getHistory(@PathVariable String ticketId) {

        return ticketService.getTicketHistory(ticketId);
    }

    @GetMapping("/search")
    public List<Ticket> searchTickets(@RequestParam String keyword) {

        return ticketService.searchTickets(keyword);
    }
}
