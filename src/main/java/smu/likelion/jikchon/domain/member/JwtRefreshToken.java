package smu.likelion.jikchon.domain.member;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import smu.likelion.jikchon.domain.member.Member;
import smu.likelion.jikchon.dto.member.TokenResponseDto;

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
    long expiredTime;

    public void updateRefreshToken(TokenResponseDto.FullInfo tokenResponseDto) {
        refreshToken = tokenResponseDto.getRefreshToken();
        expiredTime = tokenResponseDto.getRefreshTokenExpiresIn();
    }
}
