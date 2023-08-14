package smu.likelion.jikchon.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import smu.likelion.jikchon.domain.member.Member;
import smu.likelion.jikchon.domain.member.MemberRole;
import smu.likelion.jikchon.dto.member.TokenResponseDto;
import smu.likelion.jikchon.exception.CustomUnauthorizedException;
import smu.likelion.jikchon.exception.ErrorCode;
import smu.likelion.jikchon.security.JwtType;

import java.security.Key;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TokenProvider {
    private static final String TOKEN_TYPE = "Bearer";
    private static final String AUTHORITY_KEY = "auth";
    @Value("${jwt.secret-key.access}")
    private String accessTokenSecretKey;

    @Value("${jwt.secret-key.refresh}")
    private String refreshTokenSecretKey;
    private Key accessTokenKey;
    private Key refershTokenKey;

    @PostConstruct
    public void init() {
        accessTokenKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessTokenSecretKey));
        refershTokenKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshTokenSecretKey));
    }

    private String generateToken(JwtType jwtType, Long memberId, String authority, long nowMillisecond) {
        return Jwts.builder()
                .setIssuer("jikchon")
                .setSubject(memberId.toString())
                .setExpiration(new Date(nowMillisecond + jwtType.getValidMillisecond()))
                .claim(AUTHORITY_KEY, authority)
                .signWith(getKey(jwtType), SignatureAlgorithm.HS256)
                .compact();
    }

    private TokenResponseDto generateTokenResponse(JwtType jwtType, Long memberId, String authority) {
        long nowMillisecond = new Date().getTime();
        return TokenResponseDto.builder()
                .token(generateToken(jwtType, memberId, authority, nowMillisecond))
                .expiresIn((nowMillisecond + jwtType.getValidMillisecond()) / 1000L)
                .role(MemberRole.getRoleName(authority))
                .build();
    }

    public TokenResponseDto generateTokenResponse(JwtType jwtType, Authentication authentication) {
        return generateTokenResponse(jwtType,
                Long.valueOf(authentication.getName()),
                authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")));
    }

    public TokenResponseDto generateTokenResponse(JwtType jwtType, Member member) {
        return generateTokenResponse(jwtType, member.getId(), member.getAuthority().toString());
    }


    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token, accessTokenKey);

        if (ObjectUtils.isEmpty(claims.get(AUTHORITY_KEY))) {
            throw new CustomUnauthorizedException(ErrorCode.INVALID_TOKEN);
        }

        Collection<? extends GrantedAuthority> authority = Collections.singleton(new SimpleGrantedAuthority(claims.get(AUTHORITY_KEY).toString()));
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authority);
    }

    public void validateAccessToken(String token) {
        parseAccessTokenClaims(token);
    }

    public Long getMemberIdFromRefreshToken(String refreshToken) {
        return Long.valueOf(parseRefreshTokenClaims(refreshToken).getSubject());
    }

    public Claims parseAccessTokenClaims(String token) {
        return parseClaims(token, accessTokenKey);
    }

    //todo : 메서드 통합
    public Claims parseRefreshTokenClaims(String token) {
        return parseClaims(token, refershTokenKey);
    }

    private Claims parseClaims(String token, Key key) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new CustomUnauthorizedException(ErrorCode.EXPIRED_TOKEN);
        } catch (Exception e) {
            throw new CustomUnauthorizedException(ErrorCode.INVALID_TOKEN);
        }
    }

    public String getAccessToken(HttpServletRequest request) {
        String token = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION)).orElseThrow(() ->
                new CustomUnauthorizedException(ErrorCode.LOGIN_REQUIRED));

        if (!StringUtils.hasText(token) || !StringUtils.startsWithIgnoreCase(token, TOKEN_TYPE)) {
            throw new CustomUnauthorizedException(ErrorCode.INVALID_TOKEN);
        }

        return token.substring(7);
    }

    public String getRefreshToken(HttpServletRequest request) {
        String token = getCookieByName(request, JwtType.REFRESH_TOKEN.getHeader()).orElseThrow(() ->
                new CustomUnauthorizedException(ErrorCode.REFRESH_TOKEN_NOT_EXIST)
        );

        if (!StringUtils.hasText(token) || !StringUtils.startsWithIgnoreCase(token, TOKEN_TYPE)) {
            throw new CustomUnauthorizedException(ErrorCode.INVALID_TOKEN);
        }

        return token.substring(7);
    }

    private Optional<String> getCookieByName(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return Optional.of(cookie.getValue());
                }
            }
        }
        return Optional.empty();
    }

    private Key getKey(JwtType jwtType) {
        if (jwtType == JwtType.ACCESS_TOKEN) {
            return accessTokenKey;
        } else if (jwtType == JwtType.REFRESH_TOKEN) {
            return refershTokenKey;
        }
        return null;
    }
}
