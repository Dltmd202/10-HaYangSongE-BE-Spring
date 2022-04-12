package vacstage.reserve.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

public class ApiUtils {

    public static <T> ApiResult<T> success(T response){
        return new ApiResult<>(true, response, null);
    }

    public static ApiResult<?> error(String message, HttpStatus status){
        return new ApiResult<>(false, null, new ApiError(message, status));
    }



    @Getter
    public static class ApiError{
        private final String message;
        private final int status;

        ApiError(Throwable throwable, HttpStatus status){
            this(throwable.getMessage(), status);
        }

        ApiError(String message, HttpStatus status){
            this.message = message;
            this.status = status.value();
        }
    }

    @RequiredArgsConstructor
    @Getter
    public static class ApiResult<T>{
        private final boolean success;
        private final T response;
        private final ApiError error;
    }
}
