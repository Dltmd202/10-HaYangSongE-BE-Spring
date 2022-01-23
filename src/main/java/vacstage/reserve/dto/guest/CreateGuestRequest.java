package vacstage.reserve.dto.guest;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CreateGuestRequest{

    private String full_name;

    private String username;

    private int vaccine_step;

    private String password;

    private LocalDateTime vaccine_date;

    private String phone_number;
}