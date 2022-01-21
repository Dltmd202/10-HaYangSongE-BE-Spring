package vacstage.reserve.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import vacstage.reserve.domain.Guest;
import vacstage.reserve.repository.GuestRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GuestAPIController {

    private final GuestRepository guestRepository;

    @GetMapping("/guest")
    public List<Guest> guests(){
        List<Guest> guests = new ArrayList<>();
        return guests;
    }


}
