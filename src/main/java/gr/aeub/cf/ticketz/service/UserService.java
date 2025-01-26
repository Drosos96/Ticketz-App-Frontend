package gr.aeub.cf.ticketz.service;

import gr.aeub.cf.ticketz.exception.InvalidPasswordException;
import gr.aeub.cf.ticketz.exception.UserNotFoundException;
import gr.aeub.cf.ticketz.model.Role;
import gr.aeub.cf.ticketz.model.User;
import gr.aeub.cf.ticketz.model.UserRole;
import gr.aeub.cf.ticketz.repository.RoleRepository;
import gr.aeub.cf.ticketz.repository.UserRepository;
import gr.aeub.cf.ticketz.repository.UserRoleRepository;
import gr.aeub.cf.ticketz.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;


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

    public void assignRoleToUser(Integer userId, String roleName) {
        User user = findById(userId);

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));

        if (userRoleRepository.existsByUserIdAndRoleId(userId, role.getId())) {
            throw new IllegalArgumentException("User already has the role: " + roleName);
        }

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

    public void registerUser(String firstname, String lastname, String username, String email, String password, String role) {
        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);// Χρησιμοποίησε κρυπτογραφημένο κωδικό αν είναι απαραίτητο
        user.getRoles();

        registerUser(user);
    }

    private void registerUser(User user) {
    }

    public String authenticateAndGenerateToken(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        // Χρησιμοποιήστε το instance του repository
        List<String> roles = userRoleRepository.findByUserId(user.getId())
                .stream()
                .map(userRole -> userRole.getRole().getName())
                .collect(Collectors.toList());
        return jwtTokenProvider.generateToken(username, roles);
    }

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }


    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
    }

    public List<String> getUserRoles(Integer userId) {
        return userRoleRepository.findByUserId(userId) // Χρησιμοποιούμε το instance
                .stream()
                .map(userRole -> userRole.getRole().getName())
                .collect(Collectors.toList());
    }
}
