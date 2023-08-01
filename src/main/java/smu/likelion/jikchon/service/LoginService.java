package smu.likelion.jikchon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import smu.likelion.jikchon.exception.CustomForbiddenException;
import smu.likelion.jikchon.exception.ErrorCode;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    public Long getLoginMemberId() {
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
