package vacstage.reserve.dto.guest;

import lombok.AllArgsConstructor;
import lombok.Data;
import vacstage.reserve.domain.Waiting;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CreateGuestResponse {

    private Long id;

    private String username;

    private String full_name;

    private int vaccine_step;

    private String vaccine_elapsed;

    private String email;

    private LocalDateTime vaccine_date;

    private String phone_number;

    private Boolean is_staff;

    private Boolean is_host;

    private Waiting waiting_current;
}
