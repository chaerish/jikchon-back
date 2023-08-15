package smu.likelion.jikchon.dto.member;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    @Getter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Detail {
        String phoneNumber;
        String username;
        String zipcode;
        String address;
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        String companyNumber;

        public static Detail of(Member member) {
            return Detail.builder()
                    .phoneNumber(member.getFormattedPhoneNumber())
                    .username(member.getUsername())
                    .zipcode(member.getZipcode())
                    .address(member.getAddress())
                    .companyNumber(member.getCompanyNumber())
                    .build();
        }
    }
}