package vacstage.reserve.exception;

public class NotFoundRestaurantException extends RuntimeException{

    public NotFoundRestaurantException() {
        super();
    }

    public NotFoundRestaurantException(String message) {
        super(message);
    }

    public NotFoundRestaurantException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundRestaurantException(Throwable cause) {
        super(cause);
    }
}
