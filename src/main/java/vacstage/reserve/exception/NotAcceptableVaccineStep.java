package vacstage.reserve.exception;

public class NotAcceptableVaccineStep extends RuntimeException {

    public NotAcceptableVaccineStep() {
        super();
    }

    public NotAcceptableVaccineStep(String message) {
        super(message);
    }

    public NotAcceptableVaccineStep(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAcceptableVaccineStep(Throwable cause) {
        super(cause);
    }
}
