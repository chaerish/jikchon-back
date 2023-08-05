package smu.likelion.jikchon.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

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
    @OneToMany(mappedBy = "cart")
    private List<Cart> cart;


    public void encodePassword(PasswordEncoder passwordEncoder) {
        password = passwordEncoder.encode(password);
    }
}
