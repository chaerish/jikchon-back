package smu.likelion.jikchon.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import smu.likelion.jikchon.domain.member.Member;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "purchases")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    //물품, 회원
    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "status")
    private Long status;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Product product;
}