package ru.mgusev.eldritchhorror.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

public class Expansion {

    public static final String EXPANSION_FIELD_ID = "_id";
    public static final String EXPANSION_FIELD_IMAGE_RESOURCE = "image_resource";
    public static final String EXPANSION_FIELD_NAME = "name";

    @DatabaseField(dataType = DataType.INTEGER, columnName = EXPANSION_FIELD_ID)
    public int id;

    @DatabaseField(dataType = DataType.STRING, columnName = EXPANSION_FIELD_IMAGE_RESOURCE)
    public String imageResource;

    @DatabaseField(dataType = DataType.STRING, columnName = EXPANSION_FIELD_NAME)
    public String name;

    public Expansion() {
    }
}
