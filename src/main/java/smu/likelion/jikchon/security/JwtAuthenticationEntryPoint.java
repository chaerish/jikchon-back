package smu.likelion.jikchon.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import smu.likelion.jikchon.exception.CustomUnauthorizedException;
import smu.likelion.jikchon.exception.ErrorCode;


@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        throw new CustomUnauthorizedException(ErrorCode.LOGIN_REQUIRED);
    }
}
