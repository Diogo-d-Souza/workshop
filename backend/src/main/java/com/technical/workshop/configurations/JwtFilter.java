package com.technical.workshop.configurations;

import com.technical.workshop.exceptions.UnauthorizedException;
import com.technical.workshop.repositories.UserRepository;
import com.technical.workshop.service.impl.JwtServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtServiceImpl jwtServiceImpl;
    @Autowired
    private UserRepository userRepository;
    public static final String HEADER_ATTRIBUTE = "Authorization";
    public static final String PREFIX_ATTRIBUTE = "Bearer ";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String attribute = request.getHeader(HEADER_ATTRIBUTE);
        if (attribute == null) {
            filterChain.doFilter(request, response);
            return;
        }
        if (!attribute.startsWith(PREFIX_ATTRIBUTE)) {
            filterChain.doFilter(request, response);
        }

        try{
            String token = attribute.replace(PREFIX_ATTRIBUTE, "");
            String email = jwtServiceImpl.tokenValidator(token);
            if (email == null) throw new UnauthorizedException("Invalid token");
            UserDetails user = userRepository.findByEmail(email);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } catch (UnauthorizedException exception) {
            filterChain.doFilter(request, response);
        }

    }
}
