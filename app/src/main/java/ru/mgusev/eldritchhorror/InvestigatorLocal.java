package ru.mgusev.eldritchhorror;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "investigators")
public class InvestigatorLocal implements Parcelable {

    public static final String INVESTIGATOR_LOCAL_FIELD_ID = "_id";
    public static final String INVESTIGATOR_LOCAL_FIELD_IMAGE_RESOURCE = "image_resource";
    public static final String INVESTIGATOR_LOCAL_FIELD_NAME = "name";
    public static final String INVESTIGATOR_LOCAL_FIELD_OCCUPATION = "occupation";

    @DatabaseField(generatedId = true, columnName = INVESTIGATOR_LOCAL_FIELD_ID)
    public int id;

    @DatabaseField(dataType = DataType.STRING, columnName = INVESTIGATOR_LOCAL_FIELD_IMAGE_RESOURCE)
    public String imageResource;

    @DatabaseField(dataType = DataType.STRING, columnName = INVESTIGATOR_LOCAL_FIELD_NAME)
    public String name;

    @DatabaseField(dataType = DataType.STRING, columnName = INVESTIGATOR_LOCAL_FIELD_OCCUPATION)
    public String occupation;

    public InvestigatorLocal() {
    }

    public InvestigatorLocal(String imageResource, String name, String occupation) {
        this.imageResource = imageResource;
        this.name = name;
        this.occupation = occupation;
    }

    protected InvestigatorLocal(Parcel in) {
        id = in.readInt();
        imageResource = in.readString();
        name = in.readString();
        occupation = in.readString();
    }

    public static final Creator<InvestigatorLocal> CREATOR = new Creator<InvestigatorLocal>() {
        @Override
        public InvestigatorLocal createFromParcel(Parcel in) {
            return new InvestigatorLocal(in);
        }

        @Override
        public InvestigatorLocal[] newArray(int size) {
            return new InvestigatorLocal[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(imageResource);
        parcel.writeString(name);
        parcel.writeString(occupation);
    }
}
