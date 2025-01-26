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
            userService.registerUser(
                    "Admin",
                    "User",
                    adminUsername,
                    adminEmail,
                    adminPassword,
                    "ROLE_ADMIN" // Προσθέτουμε ρόλο Admin
            );
            System.out.println("Admin user created successfully!");
        } else {
            System.out.println("Admin user already exists.");
        }

        // Δημιουργία κανονικού χρήστη (User1)
        String user1Username = "user1";
        String user1Email = "user1@example.com";
        String user1Password = "password1";

        if (!userService.usernameExists(user1Username)) {
            userService.registerUser(
                    "User",
                    "One",
                    user1Username,
                    user1Email,
                    user1Password,
                    "ROLE_USER"
            );
            System.out.println("User1 created successfully!");
        } else {
            System.out.println("User1 already exists.");
        }
    }
}


