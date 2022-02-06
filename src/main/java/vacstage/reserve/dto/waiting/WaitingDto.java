package vacstage.reserve.dto.waiting;

import lombok.*;
import vacstage.reserve.constant.WaitingStatus;
import vacstage.reserve.domain.waiting.Waiting;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WaitingDto {

    private Long id;

    private Long restaurant;

    private String leader;

    private List<WaitingMemberDto> member;

    private Boolean accepted;

    private LocalDateTime date;

    public WaitingDto(Waiting waiting) {
        List<WaitingMemberDto> member = waiting.getMember().stream()
                .map(w -> w.getGuest().getUsername())
                .map(WaitingMemberDto::new)
                .collect(Collectors.toList());
        this.id = waiting.getId();
        this.restaurant = waiting.getRestaurant().getId();
        this.member = member;
        this.accepted = waiting.getWaitingStatus() == WaitingStatus.WAITING;
        this.date = waiting.getDate();
    }

    public WaitingDto(Long id, Long restaurant, String leader, WaitingStatus waitingStatus, LocalDateTime date) {
        this.id = id;
        this.restaurant = restaurant;
        this.leader = leader;
        this.accepted = waitingStatus != WaitingStatus.WAITING;
        this.date = date;
    }
}

