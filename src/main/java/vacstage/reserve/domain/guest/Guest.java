package vacstage.reserve.domain.guest;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import vacstage.reserve.domain.GuestWaiting;
import vacstage.reserve.domain.waiting.Waiting;
import vacstage.reserve.dto.guest.CreateGuestRequest;
import vacstage.reserve.dto.guest.GuestDto;
import vacstage.reserve.exception.NotMatchPasswordException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Guest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guest_id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String email;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private int vaccineStep;

    @OneToMany(mappedBy = "guest", cascade = CascadeType.ALL)
    private List<GuestWaiting> guestWaiting = new ArrayList<>();

    @OneToMany(mappedBy = "leader")
    private List<Waiting> leading = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "waiting_id")
    private Waiting currentWaiting;

    private LocalDateTime vaccineDate;

    private String phoneNumber;

    private Boolean isStaff;

    private Boolean isHost;

    @Enumerated(EnumType.STRING)
    private Authority authority;


    public void joinWaiting(GuestWaiting guestWaiting){
        this.guestWaiting.add(guestWaiting);
        guestWaiting.getWaiting().getMember().add(guestWaiting);
        setCurrentWaiting(guestWaiting.getWaiting());
    }

    public void hostWaiting(Waiting waiting){
        leading.add(waiting);
        waiting.setLeader(this);
        setCurrentWaiting(waiting);
    }

    public int getVaccineElapsed(){
        LocalDateTime now = LocalDateTime.now();
        if(vaccineDate == null){
            return 0;
        } else {
            return (int) ChronoUnit.DAYS.between(vaccineDate, now);
        }
    }

    public void checkPassword(PasswordEncoder passwordEncoder, String credentials){
        if (!passwordEncoder.matches(credentials, password)) {
            throw new NotMatchPasswordException();
        }
    }

    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(Authority.USER.toString()));

        return grantedAuthorities;
    }

    //==?????? ?????????==//
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

    //==?????? ?????????==//
    public void updateGuestInformation(GuestDto guest){
        if(guest.getUsername() != null) {
            setUsername(guest.getUsername());
        }
        if(guest.getFull_name() != null ){
            setFullName(guest.getFull_name());
        }
        if(guest.getVaccine_step() != this.vaccineStep) {
            setVaccineStep(guest.getVaccine_step());
        }
        if(guest.getEmail() != null){
            setEmail(guest.getEmail());
        }
        if(guest.getVaccine_date() != null){
            setVaccineDate(guest.getVaccine_date());
        }
        if(guest.getPhone_number() != null){
            setPhoneNumber(guest.getPhone_number());
        }
        if(guest.getIs_staff() != null){
            setIsStaff(guest.getIs_staff());
        }
        if(guest.getIs_host() != null) {
            setIsHost(guest.getIs_host());
        }
    }
}
