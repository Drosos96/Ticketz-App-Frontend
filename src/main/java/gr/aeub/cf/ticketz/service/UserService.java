package gr.aeub.cf.ticketz.service;

import gr.aeub.cf.ticketz.exception.InvalidPasswordException;
import gr.aeub.cf.ticketz.exception.UserNotFoundException;
import gr.aeub.cf.ticketz.model.Role;
import gr.aeub.cf.ticketz.model.User;
import gr.aeub.cf.ticketz.model.UserRole;
import gr.aeub.cf.ticketz.repository.RoleRepository;
import gr.aeub.cf.ticketz.repository.UserRepository;
import gr.aeub.cf.ticketz.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    // Δημιουργία χρήστη
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Εύρεση χρήστη με ID
    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }

    // Ενημέρωση στοιχείων χρήστη
    public User updateUser(Integer id, String firstname, String lastname, String currentPassword) {
        User user = findById(id);

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidPasswordException("Incorrect current password");
        }

        user.setFirstname(firstname);
        user.setLastname(lastname);

        return userRepository.save(user);
    }

    // Αλλαγή κωδικού πρόσβασης
    public void updatePassword(Integer id, String currentPassword, String newPassword, String confirmPassword) {
        User user = findById(id);

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidPasswordException("Incorrect current password");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new InvalidPasswordException("New password and confirmation do not match");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    // Ανάθεση ρόλου σε χρήστη
    public void assignRoleToUser(Integer userId, String roleName) {
        User user = findById(userId);

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));

        // Έλεγχος αν ο χρήστης έχει ήδη τον ρόλο
        Optional<UserRole> existingUserRole = userRoleRepository.findAll().stream()
                .filter(ur -> ur.getUser().getId().equals(userId) && ur.getRole().getId().equals(role.getId()))
                .findFirst();

        if (existingUserRole.isPresent()) {
            throw new IllegalArgumentException("User already has the role: " + roleName);
        }

        // Δημιουργία νέου UserRole
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);

        userRoleRepository.save(userRole);
    }

    // Αναζήτηση χρηστών με κριτήρια
    public List<User> searchUsers(String username, String email) {
        if (username != null && email != null) {
            return userRepository.findByUsernameContainingAndEmailContaining(username, email);
        } else if (username != null) {
            return userRepository.findByUsernameContaining(username);
        } else if (email != null) {
            return userRepository.findByEmailContaining(email);
        }
        return userRepository.findAll();
    }

    public User registerUser(String firstname, String lastname, String username, String email, String password) {
        // Έλεγχος αν το username είναι μοναδικό
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists.");
        }

        // Έλεγχος αν το email είναι μοναδικό
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists.");
        }

        // Δημιουργία νέου χρήστη
        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        // Απόδοση εξ ορισμού ρόλου ROLE_USER
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("Default role not found."));
        user.setRole(userRole);

        return userRepository.save(user);
    }

}
