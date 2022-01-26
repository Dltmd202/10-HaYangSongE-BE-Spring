package vacstage.reserve.dto.guest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vacstage.reserve.domain.guest.Guest;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateGuestRequest{

    private String full_name;

    private String username;

    private int vaccine_step;

    private String password;

    private LocalDateTime vaccine_date;

    private String phone_number;

    public Guest toEntity(){
        return Guest.builder()
                .username(username)
                .fullName(full_name)
                .vaccineStep(vaccine_step)
                .password(password)
                .vaccineDate(vaccine_date)
                .phoneNumber(phone_number)
                .build();
    }
}