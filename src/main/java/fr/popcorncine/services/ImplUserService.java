package fr.popcorncine.services;

import fr.popcorncine.Entities.User;
import fr.popcorncine.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service handling user management (registration, updates, etc.).
 */

@Service
public class ImplUserService implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImplAuthService authService;

    /**
     * Registers a new user in the system.
     *
     * This method checks if the email is already in use,
     * ask the AuthService to hash the password,
     * and then saves the user.
     *
     * @param user The user to be registered.
     * @throws IllegalArgumentException if the email is already in use.
     */
    @Override
    public void registerUser(User user) {
        Optional<User> existingUser =
                userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Cet email est déjà utilisé !");
        }

        user.setPassword(authService.hashPassword(user.getPassword()));

        userRepository.save(user);
    }
}
