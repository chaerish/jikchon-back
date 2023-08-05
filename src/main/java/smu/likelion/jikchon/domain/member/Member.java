package smu.likelion.jikchon.domain.member;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "members")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    MemberRole role;

    @Column(nullable = false)
    String username; //실명
    @Column(unique = true, nullable = false)
    String phoneNumber; // 전화번호 = 로그인 아이디
    @Column(nullable = false)
    String password;
    String zipcode;
    String address;
    @Column(unique = true)
    String companyNumber;
    @OneToOne(mappedBy = "member")
    JwtRefreshToken jwtRefreshToken;

    public void encodePassword(PasswordEncoder passwordEncoder) {
        password = passwordEncoder.encode(password);
    }

    public Collection<GrantedAuthority> getAuthority() {
        return Collections.singleton(new SimpleGrantedAuthority(role.toString()));
    }
}

