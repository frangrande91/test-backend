package moby.testbackend.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import moby.testbackend.model.dto.UserDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static moby.testbackend.utils.JWTUtil.JWT_HEADER;
import static moby.testbackend.utils.JWTUtil.JWT_PREFIX;
import static moby.testbackend.utils.JWTUtil.JWT_SECRET;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

    ObjectMapper objectMapper;

    public JWTAuthorizationFilter(){
        this.objectMapper = new ObjectMapper();
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try{
            if(containsJWT(request, response)){
                Claims claims = validateToken(request);
                if(claims.get("user") != null)
                    setUpSpringAuthentication(claims);
                else
                    SecurityContextHolder.clearContext();
            }
            else
                SecurityContextHolder.clearContext();
            chain.doFilter(request, response);
        }
        catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
            return;
        }
    }

    private Claims validateToken(HttpServletRequest request) {
        String jwtToken = request.getHeader(JWT_HEADER).replace(JWT_PREFIX, "");
        return Jwts.parser().setSigningKey(JWT_SECRET.getBytes()).parseClaimsJws(jwtToken).getBody();
    }

    private void setUpSpringAuthentication(Claims claims) {
        try{
            String userClaim = (String) claims.get("user");
            UserDto userDto = objectMapper.readValue(userClaim, UserDto.class);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDto, null);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (JsonProcessingException e) {
            SecurityContextHolder.clearContext();
        }
    }

    private boolean containsJWT(HttpServletRequest request, HttpServletResponse response) {
        String authenticationHeader = request.getHeader(JWT_HEADER);
        if(authenticationHeader == null || !authenticationHeader.startsWith(JWT_PREFIX))
            return false;
        return true;
    }

}
