package vacstage.reserve.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.hibernate.internal.util.StringHelper.isNotEmpty;

public class JwtAuthenticationFilter extends GenericFilterBean {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String headerKey;

    private final Jwt jwt;

    public JwtAuthenticationFilter(String headerKey, Jwt jwt) {
        this.headerKey = headerKey;
        this.jwt = jwt;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (SecurityContextHolder.getContext()
                .getAuthentication() == null) {
            String token = getToken(req);
            if (token != null) {
                try {
                    Jwt.Claims claims = verify(token);
                    Long id = claims.id;
                    List<GrantedAuthority> authorities = getAuthorities(claims);

                    if (authorities.size() > 0) {
                        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(
                                new JwtAuthentication(id, token), null, authorities
                        );
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                        SecurityContextHolder.getContext()
                                .setAuthentication(authenticationToken);
                    }
                } catch (Exception e) {
                    logger.warn("JWT 오류 발생 {}", e.getMessage());
                }
            }
        }
        chain.doFilter(req, res);
    }


    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(headerKey);
        if (isNotEmpty(token)) {
            try {
                return URLDecoder.decode(token, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private Jwt.Claims verify(String token) {
        return jwt.verify(token);
    }

    private List<GrantedAuthority> getAuthorities(Jwt.Claims claims) {
        String[] authorities = claims.authorities;
        return authorities != null && authorities.length == 0
                ? emptyList()
                : Arrays.stream(authorities)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}