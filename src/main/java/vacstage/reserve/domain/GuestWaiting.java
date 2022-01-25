package vacstage.reserve.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vacstage.reserve.domain.guest.Guest;
import vacstage.reserve.domain.waiting.Waiting;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GuestWaiting {

    @Id @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "guest_waiting_seq_generator"
    )
    @Column(name = "guest_waiting_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "guest_id")
    private Guest guest;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "waiting_id")
    private Waiting waiting;

    public static GuestWaiting createGuestWaiting(Guest guest){
        GuestWaiting guestWaiting = new GuestWaiting();
        guestWaiting.setGuest(guest);
        return guestWaiting;
    }

}
