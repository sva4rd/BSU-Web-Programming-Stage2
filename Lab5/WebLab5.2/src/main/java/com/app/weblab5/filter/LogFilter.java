package com.app.weblab5.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@Component
@Order(1)
public class LogFilter implements Filter {
    private static final Logger theLogger = LogManager.getLogger(LogFilter.class);
    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        theLogger.info(
                "Logging Request  {} : {}", req.getMethod(),
                req.getRequestURI());
        chain.doFilter(request, response);
        theLogger.info(
                "Logging Response :{}",
                res.getContentType());
    }
}