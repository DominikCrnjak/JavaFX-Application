package hr.project.exceptions;

public class SerializationFileEmptyException extends RuntimeException {

    public SerializationFileEmptyException() {
    }

    public SerializationFileEmptyException(String message) {
        super(message);
    }

    public SerializationFileEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializationFileEmptyException(Throwable cause) {
        super(cause);
    }
}
