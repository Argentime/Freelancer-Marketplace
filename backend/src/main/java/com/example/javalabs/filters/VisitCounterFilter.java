package com.example.javalabs.filters;

import com.example.javalabs.services.VisitCounterService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class VisitCounterFilter implements Filter {
    private final VisitCounterService visitCounterService;

    public VisitCounterFilter(VisitCounterService visitCounterService) {
        this.visitCounterService = visitCounterService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, jakarta.servlet.ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String url = httpRequest.getRequestURI();
        if (url.startsWith("/api/freelancers")) {
            visitCounterService.incrementVisit(url);
        }
        chain.doFilter(request, response);
    }
}