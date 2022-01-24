package vacstage.reserve.domain.waiting;

import lombok.Getter;
import lombok.Setter;
import vacstage.reserve.constant.WaitingStatus;
import vacstage.reserve.domain.GuestWaiting;
import vacstage.reserve.domain.Restaurant;
import vacstage.reserve.domain.guest.Guest;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class Waiting {

    @Id @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "guest_seq_generator"
    )
    @Column(name = "waiting_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "guest_id")
    private Guest leader;

    @OneToMany(mappedBy = "waiting", cascade = CascadeType.ALL)
    private List<GuestWaiting> member = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private WaitingStatus waitingStatus;

    private LocalDateTime date;

    public static Waiting createWaiting(
            Restaurant restaurant, Guest leader, List<GuestWaiting> guestWaitings){

        Waiting waiting = new Waiting();
        waiting.setWaitingStatus(WaitingStatus.WAITING);
        waiting.setDate(LocalDateTime.now());
        waiting.setRestaurant(restaurant);
        leader.hostWaiting(waiting);
        for (GuestWaiting guestWaiting : guestWaitings) {
            guestWaiting.setWaiting(waiting);
            guestWaiting.getGuest().joinWaiting(guestWaiting);
        }
        restaurant.registerWaiting(waiting);
        return waiting;
    }

}
