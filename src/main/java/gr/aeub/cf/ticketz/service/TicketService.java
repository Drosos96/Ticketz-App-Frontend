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

    // Δημιουργία νέου εισιτηρίου
    public Ticket createTicket(Ticket ticket) {
        ticket.setPurchaseDate(LocalDate.now());
        return ticketRepository.save(ticket);
    }

    // Εύρεση εισιτηρίου με βάση το ID
    public Ticket findById(Integer id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found with ID: " + id));
    }

    // Αναζήτηση εισιτηρίων με βάση τον αριθμό εισιτηρίου
    public List<Ticket> findByTicketNumber(String ticketNumber) {
        return ticketRepository.findByTicketNumberContaining(ticketNumber);
    }

    // Αναζήτηση εισιτηρίων με βάση το User ID
    public List<Ticket> findByUserId(Integer userId) {
        return ticketRepository.findByUserId(userId);
    }

    // Αναζήτηση εισιτηρίων με βάση το Event ID
    public List<Ticket> findByEventId(Integer eventId) {
        return ticketRepository.findByEventId(eventId);
    }

    // Διαγραφή εισιτηρίου
    public void deleteTicket(Integer id) {
        Ticket ticket = findById(id);
        ticketRepository.delete(ticket);
    }

    // Επισκόπηση όλων των εισιτηρίων
    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    // Ενημέρωση Ticket
    public Ticket updateTicket(Integer id, @Valid Ticket updatedTicketDetails) {
        // Εύρεση του ticket
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));

        // Ενημέρωση των πεδίων
        ticket.setTicketNumber(updatedTicketDetails.getTicketNumber());
        ticket.setEvent(updatedTicketDetails.getEvent());
        ticket.setUser(updatedTicketDetails.getUser());
        ticket.setPurchaseDate(updatedTicketDetails.getPurchaseDate());

        // Αποθήκευση του ενημερωμένου ticket
        return ticketRepository.save(ticket);
    }

    // Αναζήτηση Tickets με βάση eventId ή email
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
