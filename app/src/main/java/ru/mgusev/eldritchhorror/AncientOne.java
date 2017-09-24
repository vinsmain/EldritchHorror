package ru.mgusev.eldritchhorror;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "ancient_ones")
public class AncientOne {

    public static final String ANCIENT_ONE_FIELD_ID = "_id";
    public static final String ANCIENT_ONE_FIELD_IMAGE_RESOURCE = "image_resource";
    public static final String ANCIENT_ONE_FIELD_NAME = "name";

    @DatabaseField(dataType = DataType.INTEGER, columnName = ANCIENT_ONE_FIELD_ID)
    public int id;

    @DatabaseField(dataType = DataType.STRING, columnName = ANCIENT_ONE_FIELD_IMAGE_RESOURCE)
    public String imageResource;

    @DatabaseField(dataType = DataType.STRING, columnName = ANCIENT_ONE_FIELD_NAME)
    public String name;

    public AncientOne() {
    }
}
