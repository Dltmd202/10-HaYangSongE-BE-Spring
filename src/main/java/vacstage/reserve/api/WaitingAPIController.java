package vacstage.reserve.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vacstage.reserve.dto.waiting.RegisterWaitingDto;
import vacstage.reserve.dto.waiting.WaitingDto;
import vacstage.reserve.dto.wrapper.ApiResponse;
import vacstage.reserve.repository.RestaurantRepository;
import vacstage.reserve.repository.RestaurantRepositorySupport;
import vacstage.reserve.repository.WaitingRepositorySupport;
import vacstage.reserve.service.WaitingService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class WaitingAPIController {

    private final WaitingService waitingService;
    private final WaitingRepositorySupport waitingRepositorySupport;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantRepositorySupport restaurantRepositorySupport;


    @PostMapping(value = "/waiting")
    public ResponseEntity<ApiResponse<WaitingDto>> registerWaiting(
            @RequestBody @Valid RegisterWaitingDto registerWaitingDto
            ){
        Long waitingId = waitingService.waiting(registerWaitingDto);
        WaitingDto waitingDto = waitingRepositorySupport.findWaitingDto(waitingId);
        return ResponseEntity.ok(ApiResponse.of(waitingDto));
    }

    @GetMapping(value = "/waiting/{id}")
    public ResponseEntity<ApiResponse<WaitingDto>> lookUpWaiting(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(ApiResponse.of(
                waitingRepositorySupport.findWaitingDto(id)
        ));
    }
}
