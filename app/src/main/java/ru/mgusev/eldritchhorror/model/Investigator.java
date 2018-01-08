package ru.mgusev.eldritchhorror.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Locale;

@DatabaseTable(tableName = "investigators")
public class Investigator implements Parcelable {

    public static final String INVESTIGATOR_TABLE_NAME = "investigators";

    public static final String INVESTIGATOR_FIELD_ID = "_id";
    public static final String INVESTIGATOR_FIELD_GAME_ID = "game_id";
    public static final String INVESTIGATOR_FIELD_IMAGE_RESOURCE = "image_resource";
    public static final String INVESTIGATOR_FIELD_IS_MALE = "is_male";
    public static final String INVESTIGATOR_FIELD_NAME_EN = "name_en";
    public static final String INVESTIGATOR_FIELD_NAME_RU = "name_ru";
    public static final String INVESTIGATOR_FIELD_OCCUPATION_EN = "occupation_en";
    public static final String INVESTIGATOR_FIELD_OCCUPATION_RU = "occupation_ru";
    public static final String INVESTIGATOR_FIELD_IS_STARTING = "is_starting";
    public static final String INVESTIGATOR_FIELD_IS_REPLACEMENT = "is_replacement";
    public static final String INVESTIGATOR_FIELD_IS_DEAD = "is_dead";
    public static final String INVESTIGATOR_FIELD_EXPANSION_ID = "expansion_id";

    @DatabaseField(id = true, dataType = DataType.LONG, columnName = INVESTIGATOR_FIELD_ID)
    public long id;

    @DatabaseField(dataType = DataType.LONG, columnName = INVESTIGATOR_FIELD_GAME_ID)
    public long gameId;

    @DatabaseField(dataType = DataType.STRING, columnName = INVESTIGATOR_FIELD_IMAGE_RESOURCE)
    public String imageResource;

    @DatabaseField(dataType = DataType.BOOLEAN, columnName = INVESTIGATOR_FIELD_IS_MALE)
    public boolean isMale;

    @DatabaseField(dataType = DataType.STRING, columnName = INVESTIGATOR_FIELD_NAME_EN)
    public String nameEN;

    @DatabaseField(dataType = DataType.STRING, columnName = INVESTIGATOR_FIELD_NAME_RU)
    public String nameRU;

    @DatabaseField(dataType = DataType.STRING, columnName = INVESTIGATOR_FIELD_OCCUPATION_EN)
    public String occupationEN;

    @DatabaseField(dataType = DataType.STRING, columnName = INVESTIGATOR_FIELD_OCCUPATION_RU)
    public String occupationRU;

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
        id = in.readLong();
        gameId = in.readLong();
        imageResource = in.readString();
        isMale = in.readByte() != 0;
        nameEN = in.readString();
        nameRU = in.readString();
        occupationEN = in.readString();
        occupationRU = in.readString();
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
        parcel.writeLong(id);
        parcel.writeLong(gameId);
        parcel.writeString(imageResource);
        parcel.writeByte((byte) (isMale ? 1 : 0));
        parcel.writeString(nameEN);
        parcel.writeString(nameRU);
        parcel.writeString(occupationEN);
        parcel.writeString(occupationRU);
        parcel.writeByte((byte) (isStarting ? 1 : 0));
        parcel.writeByte((byte) (isReplacement ? 1 : 0));
        parcel.writeByte((byte) (isDead ? 1 : 0));
        parcel.writeInt(expansionID);
    }

    public String getName() {
        if (Localization.getInstance().isRusLocale()) return nameRU;
        else return nameEN;
    }

    public String getOccupation() {
        if (Localization.getInstance().isRusLocale()) return occupationRU;
        else return occupationEN;
    }
}