package vacstage.reserve.jwt;

import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Jwt {

    private final String issuer;
    private final String clientSecret;
    private final int expirySeconds;
    private final Algorithm algorithm;
    private final JWTVerifier jwtVerifier;

    public Jwt(String issuer, String clientSecret, int expirySeconds){
        this.issuer = issuer;
        this.clientSecret = clientSecret;
        this.expirySeconds = expirySeconds;
        this.algorithm = Algorithm.HMAC512(clientSecret);
        this.jwtVerifier = com.auth0.jwt.JWT.require(algorithm)
                .withIssuer(issuer).build();
    }

    public String sign(Claims claims){
        Date now = new Date();
        JWTCreator.Builder builder = com.auth0.jwt.JWT.create();
        builder.withIssuer(issuer);
        builder.withIssuedAt(now);
        if(expirySeconds > 0){
            builder.withExpiresAt(new Date(now.getTime() + expirySeconds * 1000L));
        }
        builder.withClaim("id", claims.id);
        builder.withArrayClaim("authorities", claims.authorities);
        return builder.sign(algorithm);
    }

    public Claims verify(String token){
        return new Claims(jwtVerifier.verify(token));
    }

    static public class Claims {
        Long id;
        String[] authorities;
        Date issuedAt;
        Date expiresAt;

        private Claims() {}

        Claims(DecodedJWT decodedJWT){
            Claim authorities = decodedJWT.getClaim("authorities");
            if( authorities != null) {
                this.authorities = authorities.asArray(String.class);
            }
            Claim id = decodedJWT.getClaim("id");
            if(id != null){
                this.id = id.asLong();
            }
            this.issuedAt = decodedJWT.getIssuedAt();
            this.expiresAt = decodedJWT.getExpiresAt();
        }

        public static Claims from(Long id, String[] authorities){
            Claims claims = new Claims();
            claims.id = id;
            claims.authorities = authorities;
            return claims;
        }

        public Map<String, Object> toMap(){
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("authorities", authorities);
            map.put("issuedAt", issuedAt);
            map.put("expiresAt", expiresAt);
            return map;
        }

        public long issuedAt(){
            return issuedAt != null ? issuedAt.getTime(): -1;
        }

        public long expiresAt(){
            return expiresAt != null ? expiresAt.getTime(): -1;
        }
    }
}
