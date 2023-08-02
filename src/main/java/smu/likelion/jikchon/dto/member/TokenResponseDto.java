package smu.likelion.jikchon.dto.member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenResponseDto {
    String accessToken;
    Long expiresIn;
    String refreshToken;
    Long refreshTokenExpiresIn;
}
