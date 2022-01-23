package vacstage.reserve.domain.guest;

import lombok.Getter;
import lombok.Setter;
import vacstage.reserve.domain.GuestWaiting;
import vacstage.reserve.domain.Waiting;
import vacstage.reserve.dto.guest.CreateGuestRequest;
import vacstage.reserve.dto.guest.GuestDto;

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


    public void joinWaiting(GuestWaiting guestWaiting){
        this.guestWaiting.add(guestWaiting);
        guestWaiting.getWaiting().getMember().add(guestWaiting);
        currentWaiting = guestWaiting.getWaiting();
    }

    public void hostWaiting(Waiting waiting){
        leading.add(waiting);
        waiting.setLeader(this);
    }

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

    //==수정 메서드==//
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
