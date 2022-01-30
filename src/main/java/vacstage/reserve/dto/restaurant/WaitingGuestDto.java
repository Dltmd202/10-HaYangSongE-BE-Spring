package vacstage.reserve.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import vacstage.reserve.domain.GuestWaiting;
import vacstage.reserve.domain.guest.Guest;

@Builder
@AllArgsConstructor
@Data
public class WaitingGuestDto {
    private Long id;

    private String username;

    public WaitingGuestDto(Guest guest){
        id = guest.getId();
        username = guest.getUsername();
    }

    public WaitingGuestDto(GuestWaiting guestWaiting) {
        id = guestWaiting.getGuest().getId();
        username = guestWaiting.getGuest().getUsername();
    }
}
