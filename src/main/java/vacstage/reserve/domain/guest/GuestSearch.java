package vacstage.reserve.domain.guest;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GuestSearch {

    private String username;

    private String email;

    private String fullName;

    private int vaccineStep;

    private String phoneNumber;

    private Boolean isStaff;

    private Boolean isHost;
}
