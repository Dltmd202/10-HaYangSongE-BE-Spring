package vacstage.reserve.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vacstage.reserve.domain.waiting.Waiting;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@Getter @Setter
public class RestaurantWaitingDto {


    private Long id;

    private LocalDateTime waiting_date;

    private WaitingGuestDto leader;

    private List<WaitingGuestDto> member;

    public RestaurantWaitingDto(Waiting waiting) {
        waiting_date = waiting.getDate();
        leader = new WaitingGuestDto(waiting.getLeader());
        member = waiting.getMember().stream()
                .map(WaitingGuestDto::new)
                .collect(Collectors.toList());
    }

    public RestaurantWaitingDto(Long id, LocalDateTime waiting_date) {
        this.id = id;
        this.waiting_date = waiting_date;
    }
}