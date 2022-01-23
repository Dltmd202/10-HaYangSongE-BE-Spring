package vacstage.reserve.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import vacstage.reserve.domain.guest.Guest;
import vacstage.reserve.domain.waiting.Waiting;


@Data
@AllArgsConstructor
public class GuestWaitingDto {

    private Long id;

    private Guest guest;

    private Waiting waiting;

}