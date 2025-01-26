package gr.aeub.cf.ticketz.service;

import gr.aeub.cf.ticketz.exception.EventNotFoundException;
import gr.aeub.cf.ticketz.exception.ResourceNotFoundException;
import gr.aeub.cf.ticketz.model.Event;
import gr.aeub.cf.ticketz.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public Event findById(Integer id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + id));
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public Event updateEvent(Integer id, Event updatedEventDetails) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));

        event.setName(updatedEventDetails.getName());
        event.setLocation(updatedEventDetails.getLocation());
        event.setDate(updatedEventDetails.getDate());
        event.setTotalTickets(updatedEventDetails.getTotalTickets());
        event.setType(updatedEventDetails.getType());

        return eventRepository.save(event);
    }

    public void deleteEvent(Integer id) {
        Event event = findById(id);
        eventRepository.delete(event);
    }

    public Optional<Event> searchByName(String name) {
        return eventRepository.findByName(name);
    }

    public List<Event> searchByLocation(String location) {
        return eventRepository.findByLocation(location);
    }

    public List<Event> searchByType(Event.EventType type) {
        return eventRepository.findByType(type);
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

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