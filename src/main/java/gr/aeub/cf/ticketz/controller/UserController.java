package gr.aeub.cf.ticketz.controller;

import gr.aeub.cf.ticketz.dto.JwtResponseDTO;
import gr.aeub.cf.ticketz.dto.LoginRequestDTO;
import gr.aeub.cf.ticketz.dto.UserDTO;
import gr.aeub.cf.ticketz.dto.UserRegistrationRequestDTO;
import gr.aeub.cf.ticketz.model.User;
import gr.aeub.cf.ticketz.repository.UserRoleRepository;
import gr.aeub.cf.ticketz.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        User user = toEntity(userDTO);
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(toDTO(createdUser), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(
            @PathVariable Integer id,
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword) {
        userService.updatePassword(id, currentPassword, newPassword, confirmPassword);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Integer id,
            @RequestParam String firstname,
            @RequestParam String lastname,
            @RequestParam String currentPassword) {
        User updatedUser = userService.updateUser(id, firstname, lastname, currentPassword);
        return ResponseEntity.ok(toDTO(updatedUser));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(toDTO(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/auth/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        String token = userService.authenticateAndGenerateToken(loginRequest.getUsername(), loginRequest.getPassword());
        User user = userService.findByUsername(loginRequest.getUsername());
        List<String> roles = userService.getUserRoles(user.getId()); // Καλέστε τη μέθοδο του service

        JwtResponseDTO response = new JwtResponseDTO(token, user.getUsername(), roles);
        return ResponseEntity.ok(response);
    }

    public List<String> getUserRoles(Integer userId) {
        return UserRoleRepository.findByUserId(userId)
                .stream()
                .map(userRole -> userRole.getRole().getName())
                .collect(Collectors.toList());
    }



    private UserDTO toDTO(User user) {
        if (user == null) return null;

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstname(user.getFirstname());
        userDTO.setLastname(user.getLastname());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }

    private User toEntity(UserDTO userDTO) {
        if (userDTO == null) return null;

        User user = new User();
        user.setId(userDTO.getId());
        user.setFirstname(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        return user;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserRegistrationRequestDTO request) {
        userService.registerUser(request.getFirstname(), request.getLastname(), request.getUsername(),
                request.getEmail(), request.getPassword(), request.getRole());
        return ResponseEntity.ok("User registered successfully!");
    }


}
