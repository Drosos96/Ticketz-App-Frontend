package gr.aeub.cf.ticketz.repository;

import gr.aeub.cf.ticketz.model.Ticket;
import gr.aeub.cf.ticketz.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    // Αναζήτηση εισιτηρίων με βάση τον αριθμό εισιτηρίου
    List<Ticket> findByTicketNumberContaining(String ticketNumber);

    // Αναζήτηση εισιτηρίων με βάση τον χρήστη
    List<Ticket> findByUserId(Integer userId);

    // Αναζήτηση εισιτηρίων με βάση το Event
    List<Ticket> findByEventId(Integer eventId);

    // Βρίσκει εισιτήρια με βάση το email του χρήστη
    List<Ticket> findByUserEmail(String email);

    // Βρίσκει εισιτήρια με βάση το ID του event και το email του χρήστη
    List<Ticket> findByEventIdAndUserEmail(Integer eventId, String email);
}
