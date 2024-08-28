package com.app.weblab5.security;

import jakarta.servlet.http.HttpSession;
import org.apache.catalina.Session;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();
        HttpSession session = request.getSession();
        session.setAttribute("username", username);
        response.sendRedirect("/CarRentalApp/home");
    }
}
