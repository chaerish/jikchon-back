package smu.likelion.jikchon.domain.member;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "verified_members")
public class VerifiedMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false)
    String phoneNumber; // 전화번호 = 로그인 아이디

    @Column(unique = true)
    String companyNumber;
}
