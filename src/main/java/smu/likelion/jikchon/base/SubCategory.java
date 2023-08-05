package smu.likelion.jikchon.base;

import lombok.AllArgsConstructor;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;

@AllArgsConstructor
public enum SubCategory {
    // 농산물
    FRUIT(Category.PRODUCE),

    VEGETABLES(Category.PRODUCE),
    MUSHROOMS(Category.PRODUCE),
    GRAINS(Category.PRODUCE),
    DRIED_PRODUCE(Category.PRODUCE),

    // 축산물
    CATTLE(Category.LIVESTOCK),
    SWINE(Category.LIVESTOCK),
    POULTRY(Category.LIVESTOCK),
    MEAT_PROCESSED(Category.LIVESTOCK),

    // 수산물
    FISH(Category.SEAFOOD),
    DRIED_FISH(Category.SEAFOOD),
    SEAWEED(Category.SEAFOOD),
    SHELLFISH_AND_AQUATIC_PLANTS(Category.SEAFOOD),
    PROCESSED_SEAFOOD(Category.SEAFOOD),

    // 가공식품
    CONDIMENTS(Category.PROCESSED_FOODS),
    SIDE_DISHES(Category.PROCESSED_FOODS),
    DAIRY_PRODUCTS(Category.PROCESSED_FOODS);

    private final Category parentCategory;
//    private final String description;
}
