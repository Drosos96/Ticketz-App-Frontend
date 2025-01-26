package gr.aeub.cf.ticketz.service;

import gr.aeub.cf.ticketz.exception.InvalidPasswordException;
import gr.aeub.cf.ticketz.exception.UserNotFoundException;
import gr.aeub.cf.ticketz.model.User;
import gr.aeub.cf.ticketz.repository.UserRepository;
import gr.aeub.cf.ticketz.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }

    public User updateUser(Integer id, String firstname, String lastname, String currentPassword) {
        User user = findById(id);

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidPasswordException("Incorrect current password");
        }

        user.setFirstname(firstname);
        user.setLastname(lastname);

        return userRepository.save(user);
    }

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

    public void registerUser(String firstname, String lastname, String username, String email, String password) {
        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        registerUser(user);
    }

    private void registerUser(User user) {
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<String> getUserRoles(Integer userId) {
        return userRoleRepository.findByUserId(userId)
                .stream()
                .map(userRole -> userRole.getRole().getName())
                .collect(Collectors.toList());
    }
}