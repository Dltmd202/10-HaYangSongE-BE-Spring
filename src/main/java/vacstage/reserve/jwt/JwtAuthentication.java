package vacstage.reserve.jwt;

import lombok.Getter;

@Getter
public class JwtAuthentication {

    private final String token;
    private final Long id;

    public JwtAuthentication(Long id, String token){
        this.id = id;
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
