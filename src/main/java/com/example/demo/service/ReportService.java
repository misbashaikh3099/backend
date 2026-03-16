package com.example.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.model.Ticket;
import com.example.demo.repository.TicketRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final TicketRepository ticketRepository;

    public Map<String, Long> getTicketStats() {

        List<Ticket> tickets = ticketRepository.findAll();

        long total = tickets.size();
        long open = tickets.stream().filter(t -> "OPEN".equals(t.getStatus())).count();
        long assigned = tickets.stream().filter(t -> "ASSIGNED".equals(t.getStatus())).count();
        long inProgress = tickets.stream().filter(t -> "IN_PROGRESS".equals(t.getStatus())).count();
        long resolved = tickets.stream().filter(t -> "RESOLVED".equals(t.getStatus())).count();
        long closed = tickets.stream().filter(t -> "CLOSED".equals(t.getStatus())).count();

        Map<String, Long> stats = new HashMap<>();

        stats.put("total", total);
        stats.put("open", open);
        stats.put("assigned", assigned);
        stats.put("inProgress", inProgress);
        stats.put("resolved", resolved);
        stats.put("closed", closed);

        return stats;
    }

    public Map<String, Long> getPriorityStats() {

        List<Ticket> tickets = ticketRepository.findAll();

        long low = tickets.stream().filter(t -> "LOW".equals(t.getPriority())).count();
        long medium = tickets.stream().filter(t -> "MEDIUM".equals(t.getPriority())).count();
        long high = tickets.stream().filter(t -> "HIGH".equals(t.getPriority())).count();
        long critical = tickets.stream().filter(t -> "CRITICAL".equals(t.getPriority())).count();

        Map<String, Long> stats = new HashMap<>();

        stats.put("low", low);
        stats.put("medium", medium);
        stats.put("high", high);
        stats.put("critical", critical);

        return stats;
    }

    public Map<String, Long> vendorPerformance() {

        List<Ticket> tickets = ticketRepository.findAll();

        Map<String, Long> performance = new HashMap<>();

        tickets.stream()
                .filter(t -> t.getVendorId() != null)
                .forEach(t -> {

                    performance.put(
                            t.getVendorId(),
                            performance.getOrDefault(t.getVendorId(), 0L) + 1
                    );

                });

        return performance;
    }

    public Map<String, Double> getSlaReport() {

        List<Ticket> tickets = ticketRepository.findAll();

        double avgResolution = tickets.stream()
                .filter(t -> t.getResolutionTimeHours() != null)
                .mapToLong(Ticket::getResolutionTimeHours)
                .average()
                .orElse(0);

        Map<String, Double> sla = new HashMap<>();

        sla.put("averageResolutionHours", avgResolution);

        return sla;
    }
}
