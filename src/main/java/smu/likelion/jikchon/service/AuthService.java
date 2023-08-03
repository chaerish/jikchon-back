package smu.likelion.jikchon.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smu.likelion.jikchon.domain.JwtRefreshToken;
import smu.likelion.jikchon.domain.Member;
import smu.likelion.jikchon.dto.member.MemberRequestDto;
import smu.likelion.jikchon.dto.member.MemberResponseDto;
import smu.likelion.jikchon.dto.member.TokenResponseDto;
import smu.likelion.jikchon.exception.CustomNotFoundException;
import smu.likelion.jikchon.exception.CustomUnauthorizedException;
import smu.likelion.jikchon.exception.ErrorCode;
import smu.likelion.jikchon.jwt.TokenProvider;
import smu.likelion.jikchon.repository.JwtRefreshTokenRepository;
import smu.likelion.jikchon.repository.MemberRepository;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final JwtRefreshTokenRepository jwtRefreshTokenRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;


    @Transactional
    public MemberResponseDto.Simple signUpCustomer(MemberRequestDto.SignUp memberRequestDto) {
        Member member = memberRequestDto.toCustomerEntity();
        member.encodePassword(passwordEncoder);

        return MemberResponseDto.Simple.of(memberRepository.save(member));
    }

    @Transactional
    public MemberResponseDto.Simple signUpSeller(MemberRequestDto.SignUp memberRequestDto) {
        Member member = memberRequestDto.toSellerEntity();
        member.encodePassword(passwordEncoder);

        return MemberResponseDto.Simple.of(memberRepository.save(member));
    }

    @Transactional
    public TokenResponseDto.AccessToken login(HttpServletResponse response, MemberRequestDto.Login loginRequestDto) {
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getPhoneNumber(), loginRequestDto.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenResponseDto.FullInfo fullTokenInfo = tokenProvider.generateTokenResponse(authentication);

        Long memberId = Long.parseLong(authentication.getName());
        createOrUpdateRefreshToken(memberId, fullTokenInfo);


        setCookieRefreshToken(response, fullTokenInfo.getRefreshToken());

        return TokenResponseDto.AccessToken.of(fullTokenInfo);
    }

    private void setCookieRefreshToken(HttpServletResponse response, String refreshToken) {

        refreshToken = "Bearer " + refreshToken;
        refreshToken = URLEncoder.encode(refreshToken, StandardCharsets.UTF_8).replaceAll("\\+", " ");

        Cookie cookie = new Cookie("Refresh_Token", refreshToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setDomain("localhost");
        cookie.setMaxAge(24*60*60);
        response.addCookie(cookie);
    }

    public void createOrUpdateRefreshToken(Long memberId, TokenResponseDto.FullInfo tokenResponseDto) {
        JwtRefreshToken refreshToken = jwtRefreshTokenRepository.findByMemberId(memberId)
                .orElse(JwtRefreshToken.builder().
                        member(Member.builder().id(memberId).build())
                        .build());

        refreshToken.updateRefreshToken(tokenResponseDto);
        jwtRefreshTokenRepository.save(refreshToken);
    }


    public TokenResponseDto.AccessToken refreshAccessToken(HttpServletRequest request) {
        String refreshToken = tokenProvider.getRefreshToken(request);
        tokenProvider.validateAccessToken(refreshToken);

        Member member = memberRepository.findByRefreshToken(refreshToken).orElseThrow(() ->
            new CustomUnauthorizedException(ErrorCode.INVALID_TOKEN)
        );

        return tokenProvider.generateTokenResponse(member);
    }

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        Member member = memberRepository.findByPhoneNumber(phoneNumber).orElseThrow(() ->
                new CustomNotFoundException(ErrorCode.NOT_FOUND)
        );

        return User.builder()
                .username(member.getId().toString())
                .password(member.getPassword())
                .authorities(member.getAuthority())
                .build();
    }
}
