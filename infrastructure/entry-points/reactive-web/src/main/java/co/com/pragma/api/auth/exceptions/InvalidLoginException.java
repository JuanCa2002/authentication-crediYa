package co.com.pragma.api.auth.exceptions;

public class InvalidLoginException extends RuntimeException{

    public InvalidLoginException(){
        super("Invalid username or password");
    }
}
