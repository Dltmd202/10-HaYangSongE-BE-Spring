package vacstage.reserve.exception;

public class GuestAlreadyHaveWaiting extends RuntimeException{

    public GuestAlreadyHaveWaiting() {
        super();
    }

    public GuestAlreadyHaveWaiting(String message) {
        super(message);
    }

    public GuestAlreadyHaveWaiting(String message, Throwable cause) {
        super(message, cause);
    }

    public GuestAlreadyHaveWaiting(Throwable cause) {
        super(cause);
    }
}
