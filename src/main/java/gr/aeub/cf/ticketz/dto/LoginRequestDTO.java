package gr.aeub.cf.ticketz.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotBlank(message = "Το username είναι υποχρεωτικό.")
    private String username;

    @NotBlank(message = "Ο κωδικός πρόσβασης είναι υποχρεωτικός.")
    private String password;

    // Getters και Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}