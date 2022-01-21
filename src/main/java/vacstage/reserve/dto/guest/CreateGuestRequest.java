package vacstage.reserve.dto.guest;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateGuestRequest{

    private String full_name;

    private String username;

    private String vaccine_step;

    private String password;

    private String photo;

    private String phone_number;
}