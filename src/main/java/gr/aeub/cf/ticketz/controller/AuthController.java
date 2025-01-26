package gr.aeub.cf.ticketz.controller;

import gr.aeub.cf.ticketz.dto.JwtResponseDTO;
import gr.aeub.cf.ticketz.dto.LoginRequestDTO;
import gr.aeub.cf.ticketz.model.User;
import gr.aeub.cf.ticketz.service.UserService;
import gr.aeub.cf.ticketz.util.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        List<String> roles = userService.getUserRoles(user.getId());

        String token = jwtTokenProvider.generateToken(username, roles);

        JwtResponseDTO response = new JwtResponseDTO(token);
        return ResponseEntity.ok(response);
    }
}
