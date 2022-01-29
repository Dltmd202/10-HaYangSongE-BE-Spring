package vacstage.reserve.dto.guest;

import lombok.*;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GuestSignInRequest {

    private String username;

    private String password;
}
