package vacstage.reserve.dto.wrapper;

import lombok.Getter;

@Getter
public class RestaurantApiListResponse<T> {

    private final int offset;
    private final int limit;
    private final String key;
    private final String district;
    private final T data;
    private double SE;
    private double WN;

    public RestaurantApiListResponse(int offset, int limit, String key, String district, T data) {
        this.offset = offset;
        this.limit = limit;
        this.key = key;
        this.district = district;
        this.data = data;
    }

    public static <T> RestaurantApiListResponse<T> of(
            T data, int offset, int limit, String key, String district){
        return new RestaurantApiListResponse<>(offset, limit, key, district, data);
    }
}
