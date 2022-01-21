package vacstage.reserve.domain;

import lombok.Getter;
import lombok.Setter;
import vacstage.reserve.dto.guest.CreateGuestRequest;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Guest {

    @Id @GeneratedValue
    @Column(name = "guest_id")
    private Long id;

    private String username;

    private String password;

    private String email;

    private String fullName;

    private int vaccineStep;

    @OneToMany(mappedBy = "guest", cascade = CascadeType.ALL)
    private List<GuestWaiting> waitings = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "waiting_id")
    private Waiting currentWaiting;

    private LocalDateTime vaccineDate;

    private String phoneNumber;

    private Boolean isStaff;

    private Boolean isHost;

    //==생성 메서드==//
    public static Guest createGuestByRequest(CreateGuestRequest request) {
        Guest guest = new Guest();
        guest.setUsername(request.getUsername());
        guest.setFullName(request.getFull_name());
        guest.setPassword(request.getPassword());
        guest.setVaccineStep(request.getVaccine_step());
        guest.setVaccineDate(request.getVaccine_date());
        guest.setPhoneNumber(request.getPhone_number());
        return guest;
    }
}
