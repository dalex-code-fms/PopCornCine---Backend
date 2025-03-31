package fr.popcorncine.Controller;

import fr.popcorncine.DTO.LoginDTO;
import fr.popcorncine.DTO.UserDTO;
import fr.popcorncine.DTO.UserUpdateDTO;
import fr.popcorncine.Entities.User;
import fr.popcorncine.Exceptions.AuthenticationException;
import fr.popcorncine.services.AuthServiceImpl;
import fr.popcorncine.services.JwtServiceImpl;
import fr.popcorncine.services.UserServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO) {
        try {
            userService.registerUser(userDTO);
            String token = this.jwtService.generateToken(userDTO.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Utilisateur enregistré avec " +
                            "succès !", "token", token));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            String token = authService.login(loginDTO.getEmail(),
                    loginDTO.getPassword());
            return ResponseEntity.ok(Map.of("token", token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmUser(@RequestParam String token, @RequestParam String email) {
        try {
            String decodedToken = URLDecoder.decode(token, StandardCharsets.UTF_8);
            userService.confirmUserEmail(decodedToken, email);
            return ResponseEntity.ok("Email confirmé avec succès !");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erreur de confirmation : " + e.getMessage());
        }
    }

    @PutMapping(value = "/update", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateUserProfile(@RequestHeader("Authorization") String token,
                                               @RequestPart("user") @Valid UserUpdateDTO userUpdateDTO,
                                               @RequestPart(value = "photo", required = false) MultipartFile photo) {
        String email = null;
        try {
            email = jwtService.extractEmail(token.replace("Bearer ", ""));
            userService.updateUserProfile(email, userUpdateDTO, photo);
            return ResponseEntity.ok(Map.of("message", "Profil mis à jour avec succès !"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du profil pour {}: {}", email, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erreur serveur lors de la mise à jour du profil"));
        }
    }

    @GetMapping(value = "/profile")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization")String token){
        try {
            String email = jwtService.extractEmail(token.replace("Bearer", ""));
            User user = userService.getUserByEmail(email);
            return ResponseEntity.ok(user);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Token invalide ou utilisateur non trouvé"));
        }
    }
}