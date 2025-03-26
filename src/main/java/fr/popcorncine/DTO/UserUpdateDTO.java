package fr.popcorncine.DTO;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDTO {
    @NotBlank(message = "Le prénom est obligatoire.")
    private String firstName;

    @NotBlank(message = "Le nom est obligatoire.")
    private String lastName;

    @Min(value = 12, message = "L'âge minimum est de 12 ans.")
    private int age;

    @NotNull(message = "Le numéro de téléphone est obligatoire.")
    @Pattern(regexp = "^(?:\\+33|0)[1-9]\\d{8}$",
            message = "Le numéro doit commencer par +33 ou 0 et contenir 9 chiffres.")
    private String phone;

    private String description;
}