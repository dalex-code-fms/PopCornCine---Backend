package fr.popcorncine.Services;

import fr.popcorncine.Entities.User;
import fr.popcorncine.Repository.UserRepository;
import fr.popcorncine.services.AuthServiceImpl;
import fr.popcorncine.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthServiceImpl authService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        // GIVEN
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("SecurePass123!");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(authService.hashPassword(anyString())).thenReturn("hashed_password");

        // WHEN
        userService.registerUser(user);

        // THEN
        assertEquals("hashed_password", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        // GIVEN
        User existingUser = new User();
        existingUser.setEmail("test@example.com");

        when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(Optional.of(existingUser));

        // WHEN & THEN
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.registerUser(existingUser)
        );
        assertEquals("Cet email est déjà utilisé !", exception.getMessage());
    }
}
