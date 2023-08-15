package smu.likelion.jikchon.dto.member;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import smu.likelion.jikchon.domain.member.Member;
import smu.likelion.jikchon.domain.member.MemberRole;

import java.util.List;


public class MemberRequestDto {

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Login {
        String phoneNumber;
        String password;
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class SignUp {
        String phoneNumber;
        String password;
        String userName;
        String email;
        String zipcode;
        String address;
        String companyNumber;

        public Member toCustomerEntity() {
            return Member.builder()
                    .phoneNumber(phoneNumber)
                    .password(password)
                    .username(userName)
                    .zipcode(zipcode)
                    .address(address)
                    .role(MemberRole.ROLE_CUSTOMER)
                    .build();
        }

        public Member toSellerEntity() {
            return Member.builder()
                    .phoneNumber(phoneNumber)
                    .password(password)
                    .username(userName)
                    .zipcode(zipcode)
                    .address(address)
                    .companyNumber(companyNumber)
                    .role(MemberRole.ROLE_SELLER)
                    .build();
        }
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class PhoneNumber {
        String phoneNumber;
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class VerifyCompanyNumber {
        String phoneNumber;
        String companyNumber;
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class InterestCategory {
        List<String> interestCategory;
    }
}
