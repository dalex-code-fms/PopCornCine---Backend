package fr.popcorncine.services;

import fr.popcorncine.Entities.User;

public interface AuthService {

     String hashPassword(String password);
     boolean verifyPassword(String rawPassword, String hashedPassword);
     User login(String email, String password);
}
