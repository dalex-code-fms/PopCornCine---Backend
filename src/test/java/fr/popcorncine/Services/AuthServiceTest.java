package fr.popcorncine.Services;

import fr.popcorncine.Repository.UserRepository;
import fr.popcorncine.services.ImplAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ImplAuthService authService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHashPassword() {
        String rawPassword = "SecurePass123!";
        String hashedPassword = authService.hashPassword(rawPassword);

        assertNotNull(hashedPassword);
        assertNotEquals(rawPassword, hashedPassword);
    }

    @Test
    void testVerifyPassword_Success() {
        String rawPassword = "SecurePass123!";
        String hashedPassword = passwordEncoder.encode(rawPassword);

        assertTrue(authService.verifyPassword(rawPassword, hashedPassword));
    }

    @Test
    void testVerifyPassword_Failure() {
        String rawPassword = "SecurePass123!";
        String wrongPassword = "WrongPass456!";
        String hashedPassword = passwordEncoder.encode(rawPassword);

        assertFalse(authService.verifyPassword(wrongPassword, hashedPassword));
    }
}
