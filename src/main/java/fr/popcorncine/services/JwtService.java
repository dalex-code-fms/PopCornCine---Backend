package fr.popcorncine.services;

import java.security.Key;

public interface JwtService {

     String generateToken(String email);
     String extractEmail(String token);
     boolean validateToken(String token, String email);
}
