package vacstage.reserve.dto;

import vacstage.reserve.domain.Guest;
import vacstage.reserve.domain.GuestWaiting;
import vacstage.reserve.domain.Restaurant;

import java.time.LocalDateTime;
import java.util.List;


public class WaitingDto {

    private Long id;

    private Restaurant restaurant;

    private Guest leader;

    private List<GuestWaiting> member;

    private Boolean accepted;

    private LocalDateTime date;

}

