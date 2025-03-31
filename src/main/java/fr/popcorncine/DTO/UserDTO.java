package fr.popcorncine.DTO;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

        @Email
        @NotNull(message = "L'email est obligatoire.")
        private String email;

//        @NotNull(message = "Le mot de passe est obligatoire.")
        @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "Le mot de passe doit contenir au moins une majuscule, une minuscule, un chiffre et un caractère spécial.")
        private String password;

        @NotBlank(message = "Le prénom est obligatoire.")
        private String firstName;

        @NotBlank(message = "Le nom est obligatoire.")
        private String lastName;

        @Min(value = 18, message = "L'âge minimum est de 18 ans.")
        private int age;

        @NotNull(message = "Le numéro de téléphone est obligatoire.")
        @Pattern(regexp = "^(?:\\+33|0)[1-9]\\d{8}$",
                message = "Le numéro doit commencer par +33 ou 0 et contenir 9 chiffres.")
        private String phone;

        private String description;

        private String photoUrl;
}
