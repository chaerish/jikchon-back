package smu.likelion.jikchon.dto.member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import smu.likelion.jikchon.domain.member.Member;

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

    @Getter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Profile {
        String name;
        String phoneNumber;
        String address;

        public static Profile of(Member member) {
            return Profile.builder()
                    .name(member.getUsername())
                    .phoneNumber(member.getFormattedPhoneNumber())
                    .address(member.getAddress())
                    .build();
        }
    }
}
