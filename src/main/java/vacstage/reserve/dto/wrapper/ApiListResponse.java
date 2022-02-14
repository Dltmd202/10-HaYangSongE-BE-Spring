package vacstage.reserve.dto.wrapper;

import lombok.Getter;

@Getter
public class ApiListResponse<T> {

    private final T data;

    private final int offset;

    private final int limit;

    public ApiListResponse(T data, int offset, int limit){
        this.data = data;
        this.offset = offset;
        this.limit = limit;
    }

    public static <T> ApiListResponse<T> of(T data, int offset, int limit){
        return new ApiListResponse<T>(data, offset, limit);
    }
}
