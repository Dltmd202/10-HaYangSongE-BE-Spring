package vacstage.reserve.domain;

import lombok.Getter;
import lombok.Setter;
import vacstage.reserve.domain.guest.Guest;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
public class Waiting {

    @Id @GeneratedValue
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

    private Boolean accepted;

    private LocalDateTime date;

}
