package vacstage.reserve.dto;

import lombok.*;
import vacstage.reserve.domain.Guest;
import vacstage.reserve.domain.Waiting;


@Data
@AllArgsConstructor
public class GuestWaitingDto {

    private Long id;

    private Guest guest;

    private Waiting waiting;

}