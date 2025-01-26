package gr.aeub.cf.ticketz.controller;

import gr.aeub.cf.ticketz.dto.EventDTO;
import gr.aeub.cf.ticketz.model.Event;
import gr.aeub.cf.ticketz.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@Valid @RequestBody EventDTO eventDTO) {
        Event event = toEntity(eventDTO);
        Event createdEvent = eventService.createEvent(event);
        return new ResponseEntity<>(toDTO(createdEvent), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable Integer id, @Valid @RequestBody EventDTO updatedEventDetails) {
        Event updatedEvent = eventService.updateEvent(id, toEntity(updatedEventDetails));
        return ResponseEntity.ok(toDTO(updatedEvent));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Integer id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Integer id) {
        Event event = eventService.findById(id);
        return ResponseEntity.ok(toDTO(event));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<EventDTO>> searchEvents(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Event.EventType type) {
        List<Event> events = eventService.searchEvents(location, type);
        List<EventDTO> eventDTOs = events.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(eventDTOs);
    }

    private EventDTO toDTO(Event event) {
        if (event == null) return null;

        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(event.getId());
        eventDTO.setName(event.getName());
        eventDTO.setLocation(event.getLocation());
        eventDTO.setDate(event.getDate());
        eventDTO.setTotalTickets(event.getTotalTickets());
        eventDTO.setType(event.getType());
        return eventDTO;
    }

    private Event toEntity(EventDTO eventDTO) {
        if (eventDTO == null) return null;

        Event event = new Event();
        event.setId(eventDTO.getId());
        event.setName(eventDTO.getName());
        event.setLocation(eventDTO.getLocation());
        event.setDate(eventDTO.getDate());
        event.setTotalTickets(eventDTO.getTotalTickets());
        event.setType(eventDTO.getType());
        return event;
    }
}
