package smu.likelion.jikchon.domain.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

@AllArgsConstructor
@Getter
public enum MemberRole {
    ROLE_CUSTOMER("customer"), ROLE_SELLER("seller");
    private final String roleName;

    public static String getRoleName(String authority) {
        return MemberRole.valueOf(authority).getRoleName();
    }
}
