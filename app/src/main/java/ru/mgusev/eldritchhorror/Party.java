package ru.mgusev.eldritchhorror;

import android.os.Parcel;
import android.os.Parcelable;

public class Party implements Parcelable {

    public String date;
    public String ancientOne;
    public int playersCount;
    public boolean isSimpleMyths;
    public boolean isNormalMyths;
    public boolean isHardMyths;
    public boolean isStartingRumor;
    public int gatesCount = 0;
    public int monstersCount = 0;
    public int curseCount = 0;
    public int rumorsCount = 0;
    public int cluesCount = 0;
    public int blessedCount = 0;
    public int doomCount = 0;
    public int score = 0;

    public Party(String date, String ancientOne, int playersCount, boolean isSimpleMyths, boolean isNormalMyths, boolean isHardMyths, boolean isStartingRumor) {
        this.date = date;
        this.ancientOne = ancientOne;
        this.playersCount = playersCount;
        this.isSimpleMyths = isSimpleMyths;
        this.isNormalMyths = isNormalMyths;
        this.isHardMyths = isHardMyths;
        this.isStartingRumor = isStartingRumor;
    }

    protected Party(Parcel in) {
        date = in.readString();
        ancientOne = in.readString();
        playersCount = in.readInt();
        isSimpleMyths = in.readByte() != 0;
        isNormalMyths = in.readByte() != 0;
        isHardMyths = in.readByte() != 0;
        isStartingRumor = in.readByte() != 0;
        gatesCount = in.readInt();
        monstersCount = in.readInt();
        curseCount = in.readInt();
        rumorsCount = in.readInt();
        cluesCount = in.readInt();
        blessedCount = in.readInt();
        doomCount = in.readInt();
        score = in.readInt();
    }

    public static final Creator<Party> CREATOR = new Creator<Party>() {
        @Override
        public Party createFromParcel(Parcel in) {
            return new Party(in);
        }

        @Override
        public Party[] newArray(int size) {
            return new Party[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(date);
        parcel.writeString(ancientOne);
        parcel.writeInt(playersCount);
        parcel.writeByte((byte) (isSimpleMyths ? 1 : 0));
        parcel.writeByte((byte) (isNormalMyths ? 1 : 0));
        parcel.writeByte((byte) (isHardMyths ? 1 : 0));
        parcel.writeByte((byte) (isStartingRumor ? 1 : 0));
        parcel.writeInt(gatesCount);
        parcel.writeInt(monstersCount);
        parcel.writeInt(curseCount);
        parcel.writeInt(rumorsCount);
        parcel.writeInt(cluesCount);
        parcel.writeInt(blessedCount);
        parcel.writeInt(doomCount);
        parcel.writeInt(score);
    }
}
