package smu.likelion.jikchon.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import smu.likelion.jikchon.exception.CustomBadRequestException;
import smu.likelion.jikchon.exception.ErrorCode;

@AllArgsConstructor
@Getter
public enum SubCategory {
    // 농산물
    FRUIT(101, Category.PRODUCE, "과일"),
    VEGETABLES(102, Category.PRODUCE, "야채"),
    MUSHROOMS(103, Category.PRODUCE, "버섯"),
    GRAINS(104, Category.PRODUCE, "곡물"),
    DRIED_PRODUCE(105, Category.PRODUCE, "건농산물"),

    // 축산물
    CATTLE(201, Category.LIVESTOCK, "소"),
    SWINE(202, Category.LIVESTOCK, "돼지"),
    POULTRY(203, Category.LIVESTOCK, "닭/오리/알류"),
    MEAT_PROCESSED(204, Category.LIVESTOCK, "육가공류"),

    // 수산물
    FISH(301, Category.SEAFOOD, "생선류"),
    DRIED_FISH(302, Category.SEAFOOD, "건어물"),
    SEAWEED(303, Category.SEAFOOD, "김/해조류"),
    SHELLFISH_AND_AQUATIC_PLANTS(304, Category.SEAFOOD, "해산물/어패류"),
    PROCESSED_SEAFOOD(305, Category.SEAFOOD, "수산가공물"),

    // 가공식품
    CONDIMENTS(401, Category.PROCESSED_FOODS, "양념류"),
    SIDE_DISHES(402, Category.PROCESSED_FOODS, "반찬류"),
    DAIRY_PRODUCTS(403, Category.PROCESSED_FOODS, "유제품");

    private final int id;
    private final Category parentCategory;
    private final String description;

    public static SubCategory fromId(Integer id) {
        if (id == null) {
            return null;
        }
        for (SubCategory subCategory : SubCategory.values()) {
            if (subCategory.getId() == id) {
                return subCategory;
            }
        }
        throw new CustomBadRequestException(ErrorCode.BAD_REQUEST);
    }

    public static SubCategory fromDescription(String description) {
        for (SubCategory subCategory : SubCategory.values()) {
            if (subCategory.getDescription().equals(description)) {
                return subCategory;
            }
        }
        throw new CustomBadRequestException(ErrorCode.BAD_REQUEST);
    }
}
