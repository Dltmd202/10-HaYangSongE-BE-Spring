package vacstage.reserve.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vacstage.reserve.domain.Restaurant;
import vacstage.reserve.domain.guest.Guest;
import vacstage.reserve.dto.api.ApiListResponse;
import vacstage.reserve.dto.api.ApiResponse;
import vacstage.reserve.dto.restaurant.RestaurantDto;
import vacstage.reserve.jwt.JwtAuthentication;
import vacstage.reserve.repository.RestaurantRepository;
import vacstage.reserve.repository.RestaurantRepositorySupport;
import vacstage.reserve.service.GuestService;
import vacstage.reserve.service.RestaurantService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RestaurantAPIController {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantRepositorySupport restaurantRepositorySupport;
    private final RestaurantService restaurantService;
    private final GuestService guestService;


    @Operation(summary = "식당 개별 조회")
    @GetMapping(value = "/restaurant/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<RestaurantDto>> find(@PathVariable("id") Long id){
        return ResponseEntity.ok(
                ApiResponse.of(restaurantRepositorySupport.findOneRestaurantDto(id)));
    }

    @Operation(summary = "식당 리스트 조회")
    @GetMapping(value = "/restaurant",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiListResponse<List<RestaurantDto>>> findAll(
        @RequestParam(value = "offset", defaultValue = "0") int offset,
        @RequestParam(value = "limit", defaultValue = "100") int limit){
        return ResponseEntity.ok(
                ApiListResponse.of(
                        restaurantRepositorySupport.findRestaurantDtos(offset, limit)
                                ,offset
                                ,limit));
    }

    @PostMapping("/restaurant/waiting/accept")
    public ResponseEntity<ApiResponse<Long>> acceptWaiting(
            @AuthenticationPrincipal JwtAuthentication token){

        Guest host = guestService.findOne(token.getId());
        Restaurant restaurant = restaurantService.findRestaurantByHostId(host.getId());
        Long acceptId = restaurantService.acceptWaiting(restaurant.getId());
        return ResponseEntity.ok(ApiResponse.of(acceptId));
    }
}
