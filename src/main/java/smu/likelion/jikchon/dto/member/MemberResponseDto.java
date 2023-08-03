package smu.likelion.jikchon.dto.member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import smu.likelion.jikchon.domain.Member;

public class MemberResponseDto {
    @Getter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Simple {
        Long id;

        public static Simple of(Member member) {
            return Simple.builder()
                    .id(member.getId())
                    .build();
        }
    }
}
