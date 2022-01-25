package vacstage.reserve.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vacstage.reserve.domain.guest.Guest;
import vacstage.reserve.domain.guest.GuestSearch;
import vacstage.reserve.dto.guest.CreateGuestRequest;
import vacstage.reserve.dto.guest.CreateGuestResponse;
import vacstage.reserve.dto.guest.GuestDto;
import vacstage.reserve.service.GuestService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class GuestAPIController {

    private final GuestService guestService;

    @Operation(summary = "게스트 탐색")
    @GetMapping("/guest/{id}")
    public GuestDto find(@PathVariable("id") Long id) {
        Guest guest  = guestService.findOne(id);
        return new GuestDto(guest);
    }

    @PostMapping("/guest")
    public CreateGuestResponse signUp(
            @RequestBody @Valid CreateGuestRequest request){
        Guest guest = Guest.createGuestByRequest(request);
        guestService.join(guest);
        return new CreateGuestResponse(guest);
    }

    @GetMapping("/guest")
    public List<GuestDto> list(){
        List<Guest> guests = guestService.findGuests(new GuestSearch());
        return guests.stream()
                .map(GuestDto::new)
                .collect(Collectors.toList());
    }



}
