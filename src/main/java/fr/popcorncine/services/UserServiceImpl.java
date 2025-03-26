package fr.popcorncine.services;

import fr.popcorncine.DTO.UserDTO;
import fr.popcorncine.DTO.UserUpdateDTO;
import fr.popcorncine.Entities.User;
import fr.popcorncine.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

/**
 * Service handling user management (registration, updates, etc.).
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private JwtServiceImpl jwtService;

    @Autowired
    private EmailService emailService;

    /**
     * Registers a new user in the system.
     *
     * This method checks if the email is already in use,
     * ask the AuthService to hash the password,
     * and then saves the user.
     *
     * @param userDTO The user to be registered.
     * @throws IllegalArgumentException if the email is already in use.
     */
    @Override
    public void registerUser(UserDTO userDTO) {
        Optional<User> existingUserByEmail =
                userRepository.findByEmail(userDTO.getEmail());

        Optional<User> existingUserByPhone =
                userRepository.findByPhone(userDTO.getPhone());


        if (existingUserByEmail.isPresent()) {
            throw new IllegalArgumentException("Cet email est déjà utilisé !");
        } else if (existingUserByPhone.isPresent()){
            throw new IllegalArgumentException("Ce nùmero de téléphone est " +
                    "déjà utilisé !");
        }

        User newUser = new User();
        newUser.setEmail(userDTO.getEmail());
        newUser.setPassword(authService.hashPassword(userDTO.getPassword()));
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setAge(userDTO.getAge());
        newUser.setPhone(userDTO.getPhone());

        userRepository.save(newUser);

        String token = jwtService.generateConfirmationToken(newUser.getEmail());
        String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);

        String confirmationLink = "http://localhost:8080/api/users/confirm" +
                "?email=" + newUser.getEmail() + "&token=" + encodedToken;


        emailService.sendConfirmationEmail(newUser.getEmail(), confirmationLink);
    }

    public void confirmUserEmail(String token, String email){
        if (!jwtService.validateConfirmationToken(token, email)){
            throw new IllegalArgumentException("Token de confirmation " +
                    "invalide ou expiré.");
        }

        User user =
                userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("Utilisateur introuvable."));
                user.setVerified(true);
                userRepository.save(user);
    }

    public void updateUserProfile(String email, UserUpdateDTO userUpdateDTO){
        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("Utilisateur introuvable"));

        user.setFirstName(userUpdateDTO.getFirstName());
        user.setLastName(userUpdateDTO.getLastName());
        user.setAge(userUpdateDTO.getAge());

        if (!user.getPhone().equals(userUpdateDTO.getPhone())) {
            Optional<User> existingUserByPhone = userRepository.findByPhone(userUpdateDTO.getPhone());
            if (existingUserByPhone.isPresent() && !existingUserByPhone.get().getId().equals(user.getId())) {
                throw new IllegalArgumentException("Ce numéro de téléphone est déjà utilisé !");
            }
            user.setPhone(userUpdateDTO.getPhone());
        }

        user.setDescription(userUpdateDTO.getDescription());

        userRepository.save(user);
    }

    public String uploadProfilePhoto(String email, MultipartFile file) throws IOException {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("Utilisateur introuvable."));

        String fileType = file.getContentType();
        if (fileType == null || !fileType.startsWith("image/")) {
            throw new IllegalArgumentException("Le fichier doit être une image !");
        }

        String uploadDir = "uploads/profile_pictures/";
        String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = email + "_" + UUID.randomUUID() + fileExtension;

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        user.setPhotoUrl("/" + uploadDir + fileName);
        userRepository.save(user);

        return filePath.toString();
    }
}
