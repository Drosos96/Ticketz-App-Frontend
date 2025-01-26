package gr.aeub.cf.ticketz.service;

import gr.aeub.cf.ticketz.dto.EventDTO;
import gr.aeub.cf.ticketz.exception.EventNotFoundException;
import gr.aeub.cf.ticketz.exception.ResourceNotFoundException;
import gr.aeub.cf.ticketz.model.Event;
import gr.aeub.cf.ticketz.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    // Εύρεση Event με βάση το ID
    public Event findById(Integer id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + id));
    }

    // Δημιουργία νέου Event
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    // Μέθοδος ενημέρωσης Event
    public Event updateEvent(Integer id, Event updatedEventDetails) {
        // Εύρεση του event από τη βάση
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));

        // Ενημέρωση των πεδίων του event
        event.setName(updatedEventDetails.getName());
        event.setLocation(updatedEventDetails.getLocation());
        event.setDate(updatedEventDetails.getDate());
        event.setTotalTickets(updatedEventDetails.getTotalTickets());
        event.setType(updatedEventDetails.getType());

        // Αποθήκευση και επιστροφή του ενημερωμένου event
        return eventRepository.save(event);
    }

    // Διαγραφή Event
    public void deleteEvent(Integer id) {
        Event event = findById(id);
        eventRepository.delete(event);
    }

    // Αναζήτηση Event με βάση το όνομα
    public Optional<Event> searchByName(String name) {
        return eventRepository.findByName(name);
    }

    // Αναζήτηση Event με βάση την τοποθεσία
    public List<Event> searchByLocation(String location) {
        return eventRepository.findByLocation(location);
    }

    // Αναζήτηση Event με βάση τον τύπο
    public List<Event> searchByType(Event.EventType type) {
        return eventRepository.findByType(type);
    }

    // Εύρεση όλων των Events
    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    // Αναζήτηση events με κριτήρια
    public List<Event> searchEvents(String location, Event.EventType type) {
        if (location != null && type != null) {
            return eventRepository.findByLocationAndType(location, type);
        } else if (location != null) {
            return eventRepository.findByLocation(location);
        } else if (type != null) {
            return eventRepository.findByType(type);
        } else {
            return eventRepository.findAll();
        }
    }
}
