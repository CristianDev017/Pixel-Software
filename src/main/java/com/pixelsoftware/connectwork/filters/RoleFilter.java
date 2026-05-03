package com.pixelsoftware.connectwork.filters;

import com.pixelsoftware.connectwork.utils.ResponseUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter({"/api/admin/*", "/api/client/*", "/api/freelancer/*"})
public class RoleFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  request  = (HttpServletRequest)  req;
        HttpServletResponse response = (HttpServletResponse) res;

        String uri  = request.getRequestURI();
        String role = (String) request.getAttribute("role");

        boolean allowed =
                (uri.contains("/api/admin/")      && "ADMIN".equals(role))      ||
                        (uri.contains("/api/client/")     && "CLIENTE".equals(role))    ||
                        (uri.contains("/api/freelancer/") && "FREELANCER".equals(role));

        if (!allowed) {
            ResponseUtil.sendForbidden(response);
            return;
        }

        chain.doFilter(req, res);
    }
}