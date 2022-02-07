package vacstage.reserve.dto.guest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class GuestToken {

    private Long id;

    private String username;

    private String token;

    private List<GrantedAuthority> authorities;

    public GuestToken(Long id, String username, String token, List<GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.token = token;
        this.authorities = authorities;
    }
}
