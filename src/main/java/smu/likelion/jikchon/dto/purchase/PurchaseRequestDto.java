package smu.likelion.jikchon.dto.purchase;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseRequestDto {
    private Long id;
    private Integer quantity;
}
