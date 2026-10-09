package crisiswallet.model;

// Checked (user-defined) exception: callers MUST handle or declare it
public class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }
}
