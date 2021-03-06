package vacstage.reserve.configure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter @Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    private String header;
    private String issuer;
    private String ClientSecret;
    private int expirySeconds;
}
