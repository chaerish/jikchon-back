package smu.likelion.jikchon.domain.enumurate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import smu.likelion.jikchon.exception.CustomBadRequestException;
import smu.likelion.jikchon.exception.ErrorCode;

@AllArgsConstructor
@Getter
public enum Category {
    PRODUCE(1, "농산물"),

    LIVESTOCK(2, "축산물"),

    SEAFOOD(3, "수산물"),

    PROCESSED_FOODS(4, "가공식품");


    private final int id;
    private final String description;

    public static Category fromId(int value) {
        for (Category category : Category.values()) {
            if (category.getId() == value) {
                return category;
            }
        }
        throw new CustomBadRequestException(ErrorCode.INVALID_PARAMETER);
    }
}
