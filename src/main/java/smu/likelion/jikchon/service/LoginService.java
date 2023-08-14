package smu.likelion.jikchon.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    public Long getLoginMemberId() {
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
