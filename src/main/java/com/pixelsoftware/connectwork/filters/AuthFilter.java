package com.pixelsoftware.connectwork.filters;

import com.pixelsoftware.connectwork.utils.JwtUtil;
import com.pixelsoftware.connectwork.utils.ResponseUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter({"/api/admin/*", "/api/client/*", "/api/freelancer/*"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  request  = (HttpServletRequest)  req;
        HttpServletResponse response = (HttpServletResponse) res;

        String token = JwtUtil.extractToken(request.getHeader("Authorization"));

        if (token == null) {
            ResponseUtil.sendUnauthorized(response);
            return;
        }

        try {
            Claims claims = JwtUtil.validateToken(token);
            request.setAttribute("userId",   Integer.parseInt(claims.getSubject()));
            request.setAttribute("username", claims.get("username", String.class));
            request.setAttribute("role",     claims.get("role",     String.class));
            chain.doFilter(req, res);
        } catch (JwtException e) {
            ResponseUtil.sendUnauthorized(response);
        }
    }
}