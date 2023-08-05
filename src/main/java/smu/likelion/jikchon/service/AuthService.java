package smu.likelion.jikchon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smu.likelion.jikchon.domain.member.Member;
import smu.likelion.jikchon.dto.member.MemberRequestDto;
import smu.likelion.jikchon.dto.member.TokenResponseDto;
import smu.likelion.jikchon.exception.CustomNotFoundException;
import smu.likelion.jikchon.exception.ErrorCode;
import smu.likelion.jikchon.jwt.TokenProvider;
import smu.likelion.jikchon.repository.MemberRepository;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;


    @Transactional
    public TokenResponseDto signUp(MemberRequestDto.SignUp memberRequestDto) {
        Member member = memberRequestDto.toEntity();
        member.encodePassword(passwordEncoder);

        memberRepository.save(member);

        return tokenProvider.generateTokenResponse(member);
    }

    @Transactional
    public TokenResponseDto login(MemberRequestDto.Login loginRequestDto) {
        Member member = memberRepository.findByPhoneNumber(loginRequestDto.getPhoneNumber()).orElseThrow(() ->
                new CustomNotFoundException(ErrorCode.NOT_FOUND));
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getPhoneNumber(), loginRequestDto.getPassword()
        );

        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return tokenProvider.generateTokenResponse(member);
    }


    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        Member member = memberRepository.findByPhoneNumber(phoneNumber).orElseThrow(() ->
                new CustomNotFoundException(ErrorCode.NOT_FOUND)
        );

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getRole().toString());

        return User.builder()
                .username(member.getId().toString())
                .password(member.getPassword())
                .authorities(Collections.singleton(grantedAuthority))
                .build();
    }
}
