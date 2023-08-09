package smu.likelion.jikchon.domain.member;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import smu.likelion.jikchon.domain.member.Member;
import smu.likelion.jikchon.dto.member.TokenResponseDto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "jwt_refresh_tokens")
public class JwtRefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    Member member;
    String refreshToken;
    ZonedDateTime expiredTime;

    public void updateRefreshToken(TokenResponseDto tokenResponseDto) {
        refreshToken = tokenResponseDto.getToken();
        //todo: systemDefault 고치기
        Instant.ofEpochSecond(tokenResponseDto.getExpiresIn()).atZone(ZoneId.systemDefault());
    }
}
