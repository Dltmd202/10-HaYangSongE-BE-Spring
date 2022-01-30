package vacstage.reserve.dto.restaurant;

import lombok.Builder;
import lombok.Data;

@Data
public class RestaurantHostDto {

    private Long id;

    private String username;

    @Builder
    public RestaurantHostDto(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}
