package me.zsj.moment.utils;

/**
 * @author zsj
 */

public class FilterUtils {

    private static final String LANDSCAPE = "风景";
    private static final String HUMANITIES = "人文";
    private static final String LOCATION = "目的地/地标";
    private static final String ARCHITECTURE_MUSEUM = "博物馆/建筑";
    private static final String ANIMAL = "动物";
    private static final String PLANT_FLOWER = "植物/花";
    private static final String LIGHT = "光线";
    private static final String WATER = "水";
    private static final String BUSINESS = "商业";
    private static final String MUSIC = "音乐";
    private static final String FOOD = "美食";
    private static final String TRADITION = "传统";
    private static final String OUTSIDE = "户外";
    private static final String MODEL = "模特/服饰";
    private static final String CREATIVE = "创意";

    public static int filterId(String text) {
        int categoryId = 0;
        switch (text) {
            case LANDSCAPE:
                categoryId = 21;
                break;
            case HUMANITIES:
                categoryId = 22;
                break;
            case LOCATION:
                categoryId = 23;
                break;
            case ARCHITECTURE_MUSEUM:
                categoryId = 30;
                break;
            case ANIMAL:
                categoryId = 39;
                break;
            case PLANT_FLOWER:
                categoryId = 40;
                break;
            case LIGHT:
                categoryId = 42;
                break;
            case WATER:
                categoryId = 43;
                break;
            case BUSINESS:
                categoryId = 44;
                break;
            case MUSIC:
                categoryId = 45;
                break;
            case FOOD:
                categoryId = 46;
                break;
            case TRADITION:
                categoryId = 47;
                break;
            case OUTSIDE:
                categoryId = 48;
                break;
            case MODEL:
                categoryId = 49;
                break;
            case CREATIVE:
                categoryId = 50;
                break;
        }
        return categoryId;
    }
}
