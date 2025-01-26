package gr.aeub.cf.ticketz.service;

import gr.aeub.cf.ticketz.exception.ResourceNotFoundException;
import gr.aeub.cf.ticketz.exception.TicketNotFoundException;
import gr.aeub.cf.ticketz.model.Ticket;
import gr.aeub.cf.ticketz.repository.TicketRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    public Ticket createTicket(Ticket ticket) {
        ticket.setPurchaseDate(LocalDate.now());
        return ticketRepository.save(ticket);
    }

    public Ticket findById(Integer id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found with ID: " + id));
    }


    public List<Ticket> findByUserId(Integer userId) {
        return ticketRepository.findByUserId(userId);
    }

    public List<Ticket> findByEventId(Integer eventId) {
        return ticketRepository.findByEventId(eventId);
    }

    public void deleteTicket(Integer id) {
        Ticket ticket = findById(id);
        ticketRepository.delete(ticket);
    }

    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    public List<Ticket> searchTickets(Integer eventId, String email) {
        List<Ticket> tickets;
        if (eventId != null && email != null) {
            tickets = ticketRepository.findByEventIdAndUserEmail(eventId, email);
        } else if (eventId != null) {
            tickets = ticketRepository.findByEventId(eventId);
        } else if (email != null) {
            tickets = ticketRepository.findByUserEmail(email);
        } else {
            tickets = ticketRepository.findAll();
        }
        return tickets;
    }
}