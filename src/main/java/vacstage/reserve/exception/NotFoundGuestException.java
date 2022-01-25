package vacstage.reserve.exception;

public class NotFoundGuestException extends RuntimeException{
    public NotFoundGuestException() {
        super();
    }

    public NotFoundGuestException(String message) {
        super(message);
    }

    public NotFoundGuestException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundGuestException(Throwable cause) {
        super(cause);
    }
}
