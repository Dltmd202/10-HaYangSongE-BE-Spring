package vacstage.reserve.domain.waiting;

import lombok.Getter;
import lombok.Setter;
import vacstage.reserve.constant.WaitingStatus;

import java.time.LocalDateTime;

@Getter @Setter
public class WaitingSearch {

    private Long restaurantId;

    private Long guestId;

    private WaitingStatus waitingStatus;

    private LocalDateTime searchFromDateTime;

    private LocalDateTime searchToDateTime;

    private int limit;
}
