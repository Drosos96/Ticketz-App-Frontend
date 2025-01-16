package gr.aeub.cf.ticketz.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TicketDTO {

    private Integer id;

    @NotNull
    @Size(max = 20)
    private String ticketNumber;

    @NotNull
    private Integer eventId;

    @NotNull
    private Integer userId;

    @NotNull
    private LocalDate purchaseDate;
}
