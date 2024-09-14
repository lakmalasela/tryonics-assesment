package com.logmaven.exmaven.filter;//
//
//package com.logmaven.exmaven.filter;
//
//import com.logmaven.exmaven.service.MyUserDetailService;
//import com.logmaven.exmaven.util.JwtUtil;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//public class JwtFilter extends UsernamePasswordAuthenticationFilter {
//
//    private final JwtUtil jwtUtil;
//    private final MyUserDetailService myUserDetailService;
//
//    public JwtFilter(JwtUtil jwtUtil, MyUserDetailService myUserDetailService) {
//        this.jwtUtil = jwtUtil;
//        this.myUserDetailService = myUserDetailService;
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7);
//            String username = jwtUtil.getUsername(token);
//            if (username != null && jwtUtil.validateToken(token)) {
//                UserDetails userDetails = myUserDetailService.loadUserByUsername(username);
//                return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//            } else {
//                System.out.println("Invalid token or username");
//            }
//        } else {
//            System.out.println("Authorization header is missing or invalid");
//        }
//        return null;
//    }
//
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//        SecurityContextHolder.getContext().setAuthentication(authResult);
//        chain.doFilter(request, response);
//    }
//
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.getWriter().write("Authentication failed: " + failed.getMessage());
//    }
//}
import com.logmaven.exmaven.service.MyUserDetailService;
import com.logmaven.exmaven.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.GenericFilterBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    private final JwtUtil jwtUtil;
    private final MyUserDetailService myUserDetailService;

    public JwtFilter(JwtUtil jwtUtil, MyUserDetailService myUserDetailService) {
        this.jwtUtil = jwtUtil;
        this.myUserDetailService = myUserDetailService;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7).trim();
            try {
                if (jwtUtil.validateToken(token)) {
                    String username = jwtUtil.getUsername(token);
                    if (username != null) {
                        UserDetails userDetails = myUserDetailService.loadUserByUsername(username);
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        logger.warn("Username not found in token");
                    }
                } else {
                    logger.warn("Invalid JWT token");
                }
            } catch (Exception e) {
                logger.error("Token validation failed: {}", e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return; // Stop further filter chain execution if token validation fails
            }
        } else {
            logger.warn("Authorization header is missing or invalid");
        }
        chain.doFilter(request, response);
    }


//    @Override
//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
//            throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) req;
//        HttpServletResponse response = (HttpServletResponse) res;
//
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7).trim(); // Ensure there's no extra whitespace
//            try {
//                if (jwtUtil.validateToken(token)) {
//                    String username = jwtUtil.getUsername(token);
//                    if (username != null) {
//                        UserDetails userDetails = myUserDetailService.loadUserByUsername(username);
//                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                        SecurityContextHolder.getContext().setAuthentication(authentication);
//                    } else {
//                        logger.warn("Username not found in token");
//                    }
//                } else {
//                    logger.warn("Invalid JWT token");
//                }
//            } catch (Exception e) {
//                logger.error("Token validation failed: {}", e.getMessage());
//            }
//        } else {
//            logger.warn("Authorization header is missing or invalid");
//        }
//        chain.doFilter(request, response);
//    }
}
