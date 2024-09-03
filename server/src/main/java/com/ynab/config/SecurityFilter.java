package com.ynab.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ynab.model.User;
import com.ynab.service.TokenService;
import com.ynab.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
        throws ServletException, IOException {
        // FIXME: WE EITHER NEED TO SET A LONG EXPIRY ON THE JWT
        //        OR WE NEED TO USE A REFRESH TOKEN
        //        SINCE CURRENTLY THE COOKIE IS JUST A SESSION COOKIE
        //        WE CAN PROBABLY GET AWAY WITH A LONG EXPIRY ON THE JWT
        //        ALSO WE NEED TO MAKE SURE THAT WE CAN'T SET MORE THAN
        //        ONE JWT COOKIE MEANING THAT WE NEED A LOGOUT ROUTE THAT
        //        CLEARS THE COOKIE SINCE THE CLIENT REDIRECTS FROM LOGIN
        //        TO BUDGET SELECT IF THE COOKIE IS PRESENT
        String token = tokenService.recoverToken(request);
        if (token != null) {
            try {
                String login = tokenService.validateToken(token);
                User user = userService.getUserByEmail(login);
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                // FIXME: MAY NEED TO SEND AS HTTP ONLY IN PROD
                // Clear the cookie if the token is invalid
                var cookie = new Cookie("jwt", "");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}
