package com.example.newsfeed.auth.filter;

import com.example.newsfeed.auth.consts.SessionConst;
import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ErrorResponse;
import com.example.newsfeed.exception.ExceptionType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class LoginFilter implements Filter {

    // 인증이 필요하지 않은 요청 URI 목록
    private static final String[] WHITE_LIST = { "/api/users/register" , "/api/auth/login" };
    private final ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();

        if (!inWhiteList(requestURI)) { // 인증 필요
            HttpSession session = httpRequest.getSession(false);

            // 인증 실패 시 401 응답 반환
            if (session == null || session.getAttribute(SessionConst.LOGIN_USER) == null) {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
                httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);

                ErrorResponse errorResponse = ErrorResponse.from(new CustomException(ExceptionType.INVALID_SESSION));
                httpResponse.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                return;
            }
        }

        // 인증 완료 시 다음 필터로 요청 전달
        chain.doFilter(request, response);
    }

    private boolean inWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }
}
