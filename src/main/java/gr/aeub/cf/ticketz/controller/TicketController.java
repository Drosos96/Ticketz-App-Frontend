package gr.aeub.cf.ticketz.controller;

import gr.aeub.cf.ticketz.dto.TicketDTO;
import gr.aeub.cf.ticketz.model.Ticket;
import gr.aeub.cf.ticketz.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TicketDTO> createTicket(@Valid @RequestBody TicketDTO ticketDTO) {
        Ticket ticket = toEntity(ticketDTO);
        Ticket createdTicket = ticketService.createTicket(ticket);
        return new ResponseEntity<>(toDTO(createdTicket), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Integer id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable Integer id) {
        Ticket ticket = ticketService.findById(id);
        return ResponseEntity.ok(toDTO(ticket));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<TicketDTO>> searchTickets(
            @RequestParam(required = false) Integer eventId,
            @RequestParam(required = false) String email) {
        List<Ticket> tickets = ticketService.searchTickets(eventId, email);
        List<TicketDTO> ticketDTOs = tickets.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ticketDTOs);
    }

    private TicketDTO toDTO(Ticket ticket) {
        if (ticket == null) return null;

        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setId(ticket.getId());
        ticketDTO.setTicketNumber(ticket.getTicketNumber());
        ticketDTO.setEventId(ticket.getEvent().getId());
        ticketDTO.setUserId(ticket.getUser().getId());
        ticketDTO.setPurchaseDate(ticket.getPurchaseDate());
        return ticketDTO;
    }

    private Ticket toEntity(TicketDTO ticketDTO) {
        if (ticketDTO == null) return null;

        Ticket ticket = new Ticket();
        ticket.setId(ticketDTO.getId());
        ticket.setTicketNumber(ticketDTO.getTicketNumber());
        ticket.setPurchaseDate(ticketDTO.getPurchaseDate());

        return ticket;
    }
}
