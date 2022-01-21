package vacstage.reserve.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GuestWaiting {

    @Id @GeneratedValue
    @Column(name = "guest_waiting_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "guest_id")
    private Guest guest;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "waiting_id")
    private Waiting waiting;



}
