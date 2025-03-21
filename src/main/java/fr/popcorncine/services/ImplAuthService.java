package fr.popcorncine.services;

import fr.popcorncine.Entities.User;
import fr.popcorncine.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service handling authentication and password security.
 */

@Service
public class ImplAuthService implements AuthService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    /**
     * Hashes a given password using BCrypt.
     *
     * @param password The raw password to be hashed.
     * @return The hashed password.
     */
    @Override
    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * Verifies if the provided password matches the stored hashed password
     *
     * @param rawPassword The plain text password provided by the user.
     * @param hashedPassword The hashed password stored in the database.
     * @return true if the password matches, false otherwise.
     */
    @Override
    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    /**
     * Authenticate a user by email and password
     *
     * @param email The user's email.
     * @param password The raw password.
     * @return The authenticated user.
     * @throws IllegalArgumentException if credentials are invalid.
     */
    @Override
    public User login(String email, String password){
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isEmpty()){
            throw new IllegalArgumentException("Cette utilisateur n'existe " +
                    "pas.");
        }

        User user = existingUser.get();

        if (!verifyPassword(password, user.getPassword())) {
            throw new IllegalArgumentException("Email ou mot de passe " +
                    "incorrects.");
        }

        return user;
    }
}
