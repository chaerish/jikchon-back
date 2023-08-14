package smu.likelion.jikchon.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import smu.likelion.jikchon.base.Category;
import smu.likelion.jikchon.base.SubCategory;
import smu.likelion.jikchon.domain.member.Member;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "orders")
public class Order extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "member_id")
    Member member;
    @OneToMany(mappedBy = "order")
    List<Purchase> purchaseList;
}
