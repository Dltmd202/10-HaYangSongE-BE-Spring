package vacstage.reserve.dto.guest;

import lombok.AllArgsConstructor;
import lombok.Data;
import vacstage.reserve.domain.guest.Guest;
import vacstage.reserve.domain.Waiting;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CreateGuestResponse {

    private Long id;

    private String username;

    private String full_name;

    private int vaccine_step;

    private int vaccine_elapsed;

    private String email;

    private LocalDateTime vaccine_date;

    private String phone_number;

    private Boolean is_staff;

    private Boolean is_host;

    private Waiting waiting_current;

    public CreateGuestResponse(Guest guest){
        this.id = guest.getId();
        this.username = guest.getUsername();
        this.full_name = guest.getFullName();
        this.vaccine_step = guest.getVaccineStep();
        this.email = guest.getEmail();
        this.vaccine_date = guest.getVaccineDate();
        this.phone_number = guest.getPhoneNumber();
        this.is_staff = guest.getIsStaff();
        this.is_host = guest.getIsHost();
        this.waiting_current = guest.getCurrentWaiting();
        this.vaccine_elapsed = calculateVaccineElapsed(guest.getVaccineDate());
    }

    public static int calculateVaccineElapsed(LocalDateTime vaccine_date){
        return 1;
    }
}
