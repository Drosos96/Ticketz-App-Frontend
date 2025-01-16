package gr.aeub.cf.ticketz.dto;

import gr.aeub.cf.ticketz.model.Event.EventType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {

    private Integer id;

    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    @Size(max = 100)
    private String location;

    @NotNull
    private LocalDate date;

    @NotNull
    private Integer totalTickets;

    @NotNull
    private EventType type;
}
