package smu.likelion.jikchon.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtType {
    ACCESS_TOKEN("Authorization", 2 * 60 * 60 * 1000L),
    REFRESH_TOKEN("Refresh-Token", 7 * 24 * 60 * 60 * 1000L);

    private final String header;
    private final long validMillisecond;
}
