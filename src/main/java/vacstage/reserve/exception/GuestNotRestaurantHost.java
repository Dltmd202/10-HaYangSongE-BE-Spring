package vacstage.reserve.exception;

public class GuestNotRestaurantHost extends RuntimeException{

    public GuestNotRestaurantHost() {
        super();
    }

    public GuestNotRestaurantHost(String message) {
        super(message);
    }

    public GuestNotRestaurantHost(String message, Throwable cause) {
        super(message, cause);
    }

    public GuestNotRestaurantHost(Throwable cause) {
        super(cause);
    }
}
