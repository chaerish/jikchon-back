package smu.likelion.jikchon.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
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
import smu.likelion.jikchon.dto.member.TokenResponseDto;
import smu.likelion.jikchon.exception.CustomUnauthorizedException;
import smu.likelion.jikchon.exception.ErrorCode;

import java.security.Key;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@Component
public class TokenProvider {
    private static final String TOKEN_TYPE = "Bearer";
    private static final String AUTHORITY_KEY = "auth";
    public static final long JWT_ACCESS_TOKEN_VALIDITY = 2 * 60 * 60 * 1000L;
    @Value("${jwt.secret-key}")
    private String secretKey;
    private Key key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String generateToken(Member member) {
        long now = new Date().getTime();
        return Jwts.builder()
                .setIssuer("jikchon")
                .setSubject(member.getId().toString())
                .claim(AUTHORITY_KEY, member.getRole())
                .setExpiration(new Date(now + JWT_ACCESS_TOKEN_VALIDITY))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public TokenResponseDto generateTokenResponse(Member member) {
        long nowSecond = new Date().getTime();
        return TokenResponseDto.builder()
                .accessToken(generateToken(member))
                .expiresIn(nowSecond + JWT_ACCESS_TOKEN_VALIDITY)
                .build();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        if (ObjectUtils.isEmpty(claims.get(AUTHORITY_KEY))) {
            throw new CustomUnauthorizedException(ErrorCode.INVALID_TOKEN);
        }

        Collection<? extends GrantedAuthority> authority = Collections.singleton(new SimpleGrantedAuthority(claims.get(AUTHORITY_KEY).toString()));
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authority);
    }

    public boolean validateToken(String token) {
        parseClaims(token);
        return true;
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new CustomUnauthorizedException(ErrorCode.EXPIRED_TOKEN);
        } catch (Exception e) {
            throw new CustomUnauthorizedException(ErrorCode.INVALID_TOKEN);
        }
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String token = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION)).orElseThrow(() ->
                new CustomUnauthorizedException(ErrorCode.LOGIN_REQUIRED));

        if (!StringUtils.hasText(token) || !StringUtils.startsWithIgnoreCase(token, TOKEN_TYPE)) {
            throw new CustomUnauthorizedException(ErrorCode.INVALID_TOKEN);
        }

        return token.substring(7);
    }
}
