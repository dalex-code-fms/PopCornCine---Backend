package fr.popcorncine.Controller;

import fr.popcorncine.DTO.UserDTO;
import fr.popcorncine.Entities.User;
import fr.popcorncine.Exceptions.AuthenticationException;
import fr.popcorncine.services.AuthServiceImpl;
import fr.popcorncine.services.JwtServiceImpl;
import fr.popcorncine.services.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private JwtServiceImpl jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO) {

        try {
            userService.registerUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Utilisateur enregistré avec " +
                            "succès !"));
        } catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "error", e.getMessage()));
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> credentials) {
        try {
            String token = authService.login(credentials.get("email"), credentials.get("password"));
            return ResponseEntity.ok(Map.of("token", token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }


}
