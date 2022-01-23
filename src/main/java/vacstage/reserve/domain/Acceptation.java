package vacstage.reserve.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vacstage.reserve.constant.WaitingStatus;
import vacstage.reserve.domain.waiting.Waiting;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Acceptation {

    @Id @GeneratedValue
    @Column(name = "acceptation_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "waiting_id")
    private Waiting waiting;

    private LocalDateTime admission_date;

    public static Acceptation createAccept(Restaurant restaurant, Waiting waiting){
        Acceptation acceptation = new Acceptation();
        acceptation.setRestaurant(restaurant);
        restaurant.getAcceptation().add(acceptation);
        acceptation.setWaiting(waiting);
        acceptation.setAdmission_date(LocalDateTime.now());
        waiting.getLeader().setCurrentWaiting(null);
        for (GuestWaiting guestWaiting : waiting.getMember()) {
            guestWaiting.getGuest().setCurrentWaiting(null);
        }
        waiting.setWaitingStatus(WaitingStatus.ACCEPT);
        return acceptation;
    }
}
