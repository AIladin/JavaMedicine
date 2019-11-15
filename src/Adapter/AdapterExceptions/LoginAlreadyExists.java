package Adapter.AdapterExceptions;

public class LoginAlreadyExists extends Exception {
    public LoginAlreadyExists(String message) {
        super(message);
    }
}
