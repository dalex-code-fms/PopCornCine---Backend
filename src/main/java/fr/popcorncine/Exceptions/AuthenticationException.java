package fr.popcorncine.Exceptions;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message){
        super(message);
    }
}
