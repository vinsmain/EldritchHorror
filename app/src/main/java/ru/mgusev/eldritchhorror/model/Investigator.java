package ru.mgusev.eldritchhorror.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "investigators")
public class Investigator implements Parcelable {
    public static final String INVESTIGATOR_FIELD_ID = "_id";
    public static final String INVESTIGATOR_FIELD_GAME_ID = "game_id";
    public static final String INVESTIGATOR_FIELD_IMAGE_RESOURCE = "image_resource";
    public static final String INVESTIGATOR_FIELD_IS_MALE = "is_male";
    public static final String INVESTIGATOR_FIELD_NAME = "name";
    public static final String INVESTIGATOR_FIELD_OCCUPATION = "occupation";
    public static final String INVESTIGATOR_FIELD_IS_STARTING = "is_starting";
    public static final String INVESTIGATOR_FIELD_IS_REPLACEMENT = "is_replacement";
    public static final String INVESTIGATOR_FIELD_IS_DEAD = "is_dead";
    public static final String INVESTIGATOR_FIELD_EXPANSION_ID = "expansion_id";

    @DatabaseField(dataType = DataType.INTEGER, generatedId = true, columnName = INVESTIGATOR_FIELD_ID)
    public int id;

    @DatabaseField(dataType = DataType.INTEGER, columnName = INVESTIGATOR_FIELD_GAME_ID)
    public int gameId;

    @DatabaseField(dataType = DataType.STRING, columnName = INVESTIGATOR_FIELD_IMAGE_RESOURCE)
    public String imageResource;

    @DatabaseField(dataType = DataType.BOOLEAN, columnName = INVESTIGATOR_FIELD_IS_MALE)
    public boolean isMale;

    @DatabaseField(dataType = DataType.STRING, columnName = INVESTIGATOR_FIELD_NAME)
    public String name;

    @DatabaseField(dataType = DataType.STRING, columnName = INVESTIGATOR_FIELD_OCCUPATION)
    public String occupation;

    @DatabaseField(dataType = DataType.BOOLEAN, columnName = INVESTIGATOR_FIELD_IS_STARTING)
    public boolean isStarting;

    @DatabaseField(dataType = DataType.BOOLEAN, columnName = INVESTIGATOR_FIELD_IS_REPLACEMENT)
    public boolean isReplacement;

    @DatabaseField(dataType = DataType.BOOLEAN, columnName = INVESTIGATOR_FIELD_IS_DEAD)
    public boolean isDead;

    @DatabaseField(dataType = DataType.INTEGER, columnName = INVESTIGATOR_FIELD_EXPANSION_ID)
    public int expansionID;

    public Investigator() {
    }

    protected Investigator(Parcel in) {
        id = in.readInt();
        gameId = in.readInt();
        imageResource = in.readString();
        isMale = in.readByte() != 0;
        name = in.readString();
        occupation = in.readString();
        isStarting = in.readByte() != 0;
        isReplacement = in.readByte() != 0;
        isDead = in.readByte() != 0;
        expansionID = in.readInt();
    }

    public static final Creator<Investigator> CREATOR = new Creator<Investigator>() {
        @Override
        public Investigator createFromParcel(Parcel in) {
            return new Investigator(in);
        }

        @Override
        public Investigator[] newArray(int size) {
            return new Investigator[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(gameId);
        parcel.writeString(imageResource);
        parcel.writeByte((byte) (isMale ? 1 : 0));
        parcel.writeString(name);
        parcel.writeString(occupation);
        parcel.writeByte((byte) (isStarting ? 1 : 0));
        parcel.writeByte((byte) (isReplacement ? 1 : 0));
        parcel.writeByte((byte) (isDead ? 1 : 0));
        parcel.writeInt(expansionID);
    }
}
