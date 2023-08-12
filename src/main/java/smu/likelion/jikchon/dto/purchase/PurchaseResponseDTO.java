package smu.likelion.jikchon.dto.purchase;

import lombok.*;
import smu.likelion.jikchon.domain.Purchase;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseResponseDTO {
    private Long id;
    private String productName;
    private Long price;
    private Long quantity;
    private String imageUrl;
}
