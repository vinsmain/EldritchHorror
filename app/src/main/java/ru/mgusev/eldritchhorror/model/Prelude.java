package ru.mgusev.eldritchhorror.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "preludes")
public class Prelude {

    public static final String PRELUDE_FIELD_ID = "_id";
    public static final String PRELUDE_FIELD_NAME_EN = "name_en";
    public static final String PRELUDE_FIELD_NAME_RU = "name_ru";
    public static final String PRELUDE_FIELD_EXPANSION_ID = "expansion_id";

    @DatabaseField(dataType = DataType.INTEGER, generatedId = true, columnName = PRELUDE_FIELD_ID)
    public int id;

    @DatabaseField(dataType = DataType.STRING, columnName = PRELUDE_FIELD_NAME_EN)
    public String nameEN;

    @DatabaseField(dataType = DataType.STRING, columnName = PRELUDE_FIELD_NAME_RU)
    public String nameRU;

    @DatabaseField(dataType = DataType.INTEGER, columnName = PRELUDE_FIELD_EXPANSION_ID)
    public int expansionID;

    public Prelude() {
    }

    public String getName() {
        if (Localization.getInstance().isRusLocale()) return nameRU;
        else return nameEN;
    }
}