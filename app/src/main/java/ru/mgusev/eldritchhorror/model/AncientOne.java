package ru.mgusev.eldritchhorror.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Locale;

@DatabaseTable(tableName = "ancient_ones")
public class AncientOne {

    public static final String ANCIENT_ONE_FIELD_ID = "_id";
    public static final String ANCIENT_ONE_FIELD_IMAGE_RESOURCE = "image_resource";
    public static final String ANCIENT_ONE_FIELD_NAME_EN = "name_en";
    public static final String ANCIENT_ONE_FIELD_NAME_RU = "name_ru";
    public static final String ANCIENT_ONE_EXPANSION_ID = "expansion_id";

    @DatabaseField(dataType = DataType.INTEGER, generatedId = true, columnName = ANCIENT_ONE_FIELD_ID)
    public int id;

    @DatabaseField(dataType = DataType.STRING, columnName = ANCIENT_ONE_FIELD_IMAGE_RESOURCE)
    public String imageResource;

    @DatabaseField(dataType = DataType.STRING, columnName = ANCIENT_ONE_FIELD_NAME_EN)
    public String nameEN;

    @DatabaseField(dataType = DataType.STRING, columnName = ANCIENT_ONE_FIELD_NAME_RU)
    public String nameRU;

    @DatabaseField(dataType = DataType.INTEGER, columnName = ANCIENT_ONE_EXPANSION_ID)
    public int expansionID;

    public AncientOne() {
    }

    public String getName() {
        if (Locale.getDefault().getLanguage().equals("ru")) return nameRU;
        else return nameEN;
    }
}
