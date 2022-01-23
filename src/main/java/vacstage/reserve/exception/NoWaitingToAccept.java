package vacstage.reserve.exception;

public class NoWaitingToAccept extends RuntimeException{
    public NoWaitingToAccept() {
        super();
    }

    public NoWaitingToAccept(String message) {
        super(message);
    }

    public NoWaitingToAccept(String message, Throwable cause) {
        super(message, cause);
    }

    public NoWaitingToAccept(Throwable cause) {
        super(cause);
    }
}
