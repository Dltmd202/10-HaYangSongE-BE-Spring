package vacstage.reserve.dto.guest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vacstage.reserve.domain.guest.Guest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateGuestRequest{

    @NotBlank
    private String full_name;

    @NotBlank
    private String username;

    @NotNull
    private int vaccine_step;

    @NotBlank
    private String password;

    @NotNull
    private LocalDateTime vaccine_date;

    @NotBlank
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