package fr.popcorncine.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "t_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotNull(message = "L'email est obligatoire.")
    @Column(unique = true)
    private String email;

    @NotNull(message = "Le mot de passe est obligatoire.")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Le mot de passe doit contenir au moins une majuscule, une minuscule, un chiffre et un caractère spécial.")
    private String password;

    @NotBlank(message = "Le nom est obligatoire.")
    private String lastName;

    @NotBlank(message = "Le prénom est obligatoire.")
    private String firstName;

    @Min(value = 12, message = "L'âge minimum est de 12 ans.")
    private int age;

    @NotNull(message = "Le numéro de téléphone est obligatoire.")
    @Column(unique = true)
    @Pattern(regexp = "^(?:\\+33|0)[1-9]\\d{8}$",
            message = "Le numéro doit commencer par +33 ou 0 et contenir 9 chiffres.")
    private String phone;

    private boolean isVerified;
    private boolean isPremium;
    private boolean isIdentityVerified;

    private String photoUrl;
    private String description;

    @ElementCollection
    private List<String> preferences;
}
