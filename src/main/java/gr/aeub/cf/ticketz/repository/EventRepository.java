package gr.aeub.cf.ticketz.repository;

import gr.aeub.cf.ticketz.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    // Custom Query Method για αναζήτηση event με βάση το όνομα
    Optional<Event> findByName(String name);

    // Custom Query Method για αναζήτηση event με βάση την τοποθεσία
    List<Event> findByLocation(String location);

    // Custom Query Method για εύρεση events με βάση το είδος
    List<Event> findByType(Event.EventType type);

    // Custom Query Method για αναζήτηση event με βάση την τοποθεσία και το είδος
    List<Event> findByLocationAndType(String location, Event.EventType type);
}
