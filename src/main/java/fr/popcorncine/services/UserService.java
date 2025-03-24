package fr.popcorncine.services;

import fr.popcorncine.DTO.UserDTO;
import fr.popcorncine.Entities.User;

public interface UserService {

    public void registerUser(UserDTO userDTO);
}
