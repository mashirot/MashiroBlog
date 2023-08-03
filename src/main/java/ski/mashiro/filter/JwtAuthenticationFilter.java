package ski.mashiro.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ski.mashiro.common.JwtInfo;
import ski.mashiro.util.JwtUtils;

import java.io.IOException;
import java.util.Objects;

import static ski.mashiro.constant.RedisConstant.USER_JWT_KEY;

/**
 * @author MashiroT
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final StringRedisTemplate redisTemplate;

    public JwtAuthenticationFilter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (!StringUtils.hasText(authorization)) {
            filterChain.doFilter(request, response);
            return;
        }
        String jwt = authorization.split(" ")[1];
        try {
            DecodedJWT decodedJWT = JwtUtils.parseJwt(jwt);
            String jwtId = decodedJWT.getClaim("jwtId").asString();
            if (Objects.isNull(redisTemplate.opsForValue().get(USER_JWT_KEY + jwtId))) {
                filterChain.doFilter(request, response);
                return;
            }
            Long id = decodedJWT.getClaim("id").asLong();
            String username = decodedJWT.getClaim("username").asString();
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(new JwtInfo(jwtId, id, username), null, null);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } catch (JWTVerificationException e) {
            filterChain.doFilter(request, response);
        }
    }
}
