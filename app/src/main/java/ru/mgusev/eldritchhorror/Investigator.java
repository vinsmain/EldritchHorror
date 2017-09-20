package ru.mgusev.eldritchhorror;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "investigators")
public class Investigator {

    public static final String INVESTIGATOR_FIELD_ID = "_id";
    public static final String INVESTIGATOR_FIELD_IMAGE_RESOURCE = "image_resource";
    public static final String INVESTIGATOR_FIELD_NAME = "name";
    public static final String INVESTIGATOR_FIELD_OCCUPATION = "occupation";
    public static final String INVESTIGATOR_FIELD_SELECTED = "selected";
    public static final String INVESTIGATOR_FIELD_STARTING = "starting";
    public static final String INVESTIGATOR_FIELD_SURVIVED = "survived";

    @DatabaseField(generatedId = true, columnName = INVESTIGATOR_FIELD_ID)
    public int id;

    @DatabaseField(dataType = DataType.STRING, columnName = INVESTIGATOR_FIELD_IMAGE_RESOURCE)
    public String imageResource;

    @DatabaseField(dataType = DataType.STRING, columnName = INVESTIGATOR_FIELD_NAME)
    public String name;

    @DatabaseField(dataType = DataType.STRING, columnName = INVESTIGATOR_FIELD_OCCUPATION)
    public String occupation;

    @DatabaseField(dataType = DataType.BOOLEAN, columnName = INVESTIGATOR_FIELD_SELECTED)
    public boolean isSelected;

    @DatabaseField(dataType = DataType.BOOLEAN, columnName = INVESTIGATOR_FIELD_STARTING)
    public boolean isStarting;

    @DatabaseField(dataType = DataType.BOOLEAN, columnName = INVESTIGATOR_FIELD_SURVIVED)
    public boolean isSurvived;

    public Investigator() {
    }

    public Investigator(String imageResource, String name, String occupation, boolean isSelected, boolean isStarting, boolean isSurvived) {
        this.imageResource = imageResource;
        this.name = name;
        this.occupation = occupation;
        this.isSelected = isSelected;
        this.isStarting = isStarting;
        this.isSurvived = isSurvived;
    }
}
