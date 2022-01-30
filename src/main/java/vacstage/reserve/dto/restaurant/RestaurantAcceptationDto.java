package vacstage.reserve.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vacstage.reserve.domain.Acceptation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@Getter @Setter
public class RestaurantAcceptationDto {

    private LocalDateTime admission_date;

    private Long waiting_id;

    private LocalDateTime waiting_date;

    private WaitingGuestDto leader;

    private List<WaitingGuestDto> member;

    public RestaurantAcceptationDto(Acceptation acceptation) {
        admission_date = acceptation.getAdmission_date();
        waiting_date = acceptation.getWaiting().getDate();
        leader = new WaitingGuestDto(acceptation.getWaiting().getLeader());
        member = acceptation.getWaiting().getMember().stream()
                .map(WaitingGuestDto::new)
                .collect(Collectors.toList());

    }

    public RestaurantAcceptationDto(LocalDateTime admission_date, LocalDateTime waiting_date, WaitingGuestDto leader, List<WaitingGuestDto> member) {
        this.admission_date = admission_date;
        this.waiting_date = waiting_date;
        this.leader = leader;
        this.member = member;
    }

    public RestaurantAcceptationDto(LocalDateTime admission_date, Long waiting_id, LocalDateTime waiting_date) {
        this.admission_date = admission_date;
        this.waiting_date = waiting_date;
        this.waiting_id = waiting_id;
    }
}
