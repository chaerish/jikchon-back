package smu.likelion.jikchon.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;

@AllArgsConstructor
@Getter
public enum SubCategory {
    // 농산물
    FRUIT(Category.PRODUCE,"과일"),

    VEGETABLES(Category.PRODUCE,"야채"),
    MUSHROOMS(Category.PRODUCE,"버섯"),
    GRAINS(Category.PRODUCE,"곡물"),
    DRIED_PRODUCE(Category.PRODUCE,"건농산물"),

    // 축산물
    CATTLE(Category.LIVESTOCK,"소"),
    SWINE(Category.LIVESTOCK,"돼지"),
    POULTRY(Category.LIVESTOCK,"닭/오리/알류"),
    MEAT_PROCESSED(Category.LIVESTOCK,"육가공류"),

    // 수산물
    FISH(Category.SEAFOOD,"생선류"),
    DRIED_FISH(Category.SEAFOOD,"건어물"),
    SEAWEED(Category.SEAFOOD,"김/해조류"),
    SHELLFISH_AND_AQUATIC_PLANTS(Category.SEAFOOD,"해산물/어패류"),
    PROCESSED_SEAFOOD(Category.SEAFOOD,"수산가공물"),

    // 가공식품
    CONDIMENTS(Category.PROCESSED_FOODS,"양념류"),
    SIDE_DISHES(Category.PROCESSED_FOODS,"반찬류"),
    DAIRY_PRODUCTS(Category.PROCESSED_FOODS,"유제품");

    private final Category parentCategory;
    private final String description;
}
