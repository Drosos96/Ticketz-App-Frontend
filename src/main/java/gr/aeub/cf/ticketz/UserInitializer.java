package gr.aeub.cf.ticketz;

import gr.aeub.cf.ticketz.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserInitializer implements CommandLineRunner {

    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {
        // Δημιουργία χρήστη διαχειριστή (Admin)
        String adminUsername = "admin";
        String adminEmail = "admin@example.com";
        String adminPassword = "admin123";

        if (!userService.usernameExists(adminUsername)) {
            userService.registerUser("Admin", "User", adminUsername, adminEmail, adminPassword);
            System.out.println("Admin user created successfully!");
        } else {
            System.out.println("Admin user already exists.");
        }
    }
}
