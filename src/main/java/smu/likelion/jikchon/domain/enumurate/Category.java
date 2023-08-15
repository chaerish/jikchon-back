package smu.likelion.jikchon.domain.enumurate;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Category {
    PRODUCE(1, "농산물"),

    LIVESTOCK(2, "축산물"),

    SEAFOOD(3, "수산물"),

    PROCESSED_FOODS(4, "가공식품");

    private final int id;
    private final String description;
}
