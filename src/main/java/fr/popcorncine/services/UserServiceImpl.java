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
import java.util.UUID;

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

    private static final String UPLOAD_DIR = "uploads/profile_pictures/";

    @Override
    public void registerUser(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Cet email est déjà utilisé !");
        }
        if (userRepository.findByPhone(userDTO.getPhone()).isPresent()) {
            throw new IllegalArgumentException("Ce numéro de téléphone est déjà utilisé !");
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
        String confirmationLink = "http://localhost:8080/api/users/confirm?email=" +
                newUser.getEmail() + "&token=" + URLEncoder.encode(token, StandardCharsets.UTF_8);
        emailService.sendConfirmationEmail(newUser.getEmail(), confirmationLink);
    }

    public void confirmUserEmail(String token, String email) {
        if (!jwtService.validateConfirmationToken(token, email)) {
            throw new IllegalArgumentException("Token de confirmation invalide ou expiré");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));
        user.setVerified(true);
        userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));
    }

    public void updateUserProfile(String email, UserUpdateDTO userUpdateDTO, MultipartFile photo) throws IOException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        user.setFirstName(userUpdateDTO.getFirstName());
        user.setLastName(userUpdateDTO.getLastName());
        user.setAge(userUpdateDTO.getAge());

        String newPhone = userUpdateDTO.getPhone();
        if (newPhone != null && !newPhone.equals(user.getPhone())) {
            if (userRepository.findByPhone(newPhone).filter(u -> !u.getId().equals(user.getId())).isPresent()) {
                throw new IllegalArgumentException("Ce numéro de téléphone est déjà utilisé !");
            }
            user.setPhone(newPhone);
        }

        user.setDescription(userUpdateDTO.getDescription());

        if (photo != null && !photo.isEmpty()) {
            String contentType = photo.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("Le fichier doit être une image");
            }

            Path uploadPath = Paths.get(UPLOAD_DIR);
            Files.createDirectories(uploadPath);

            String fileExtension = photo.getOriginalFilename().substring(photo.getOriginalFilename().lastIndexOf("."));
            String fileName = UUID.randomUUID() + fileExtension;
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            user.setPhotoUrl("/" + UPLOAD_DIR + fileName);
        }
        userRepository.save(user);
    }
}