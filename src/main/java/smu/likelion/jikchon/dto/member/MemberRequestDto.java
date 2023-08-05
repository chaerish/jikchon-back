package smu.likelion.jikchon.dto.member;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import smu.likelion.jikchon.domain.member.Member;
import smu.likelion.jikchon.domain.MemberRole;

public class MemberRequestDto {

    @Getter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Login {
        String phoneNumber;
        String password;
    }


    @Getter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class SignUp {
        String phoneNumber;
        String password;
        String userName;
        String zipcode;
        String address;

        public Member toEntity() {
            return Member.builder()
                    .phoneNumber(phoneNumber)
                    .password(password)
                    .username(userName)
                    .zipcode(zipcode)
                    .address(address)
                    .role(MemberRole.ROLE_CUSTOMER)
                    .build();
        }
    }
}
