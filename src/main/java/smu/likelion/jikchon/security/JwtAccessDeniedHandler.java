package smu.likelion.jikchon.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import smu.likelion.jikchon.exception.CustomForbiddenException;
import smu.likelion.jikchon.exception.ErrorCode;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        throw new CustomForbiddenException(ErrorCode.FORBIDDEN);
    }
}
