package com.system.lms.fo.filter;

import com.system.lms.fo.common.CookieBuilder;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.parser.Authorization;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.LogRecord;

public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        Optional<String> jwtToken = Optional.ofNullable(request.getCookies())
                .flatMap(cookies -> Arrays.stream(cookies)
                        .filter(cookie -> "jwtToken".equals(cookie.getName()))
                        .map(Cookie::getValue)
                        .findFirst());

        boolean isLogin = jwtToken.isPresent();

        request.setAttribute("isLogin", isLogin);

        jwtToken.ifPresent(token -> request.setAttribute("jwtToken", token));

        filterChain.doFilter(request, response);
    }
}