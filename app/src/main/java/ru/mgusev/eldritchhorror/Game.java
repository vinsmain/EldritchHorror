package ru.mgusev.eldritchhorror;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

@DatabaseTable(tableName = "games")
public class Game implements Parcelable {

    public static final String GAME_FIELD_ID = "_id";
    public static final String GAME_FIELD_DATE = "date";
    public static final String GAME_FIELD_ANCIENT_ONE_ID = "ancient_one_id";
    public static final String GAME_FIELD_PLAYERS_COUNT = "players_count";
    public static final String GAME_FIELD_SIMPLE_MYTHS = "simple_myths";
    public static final String GAME_FIELD_NORMAL_MYTHS = "normal_myths";
    public static final String GAME_FIELD_HARD_MYTHS = "hard_myths";
    public static final String GAME_FIELD_STARTING_RUMOR = "starting_rumor";
    public static final String GAME_FIELD_GATES_COUNT = "gates_count";
    public static final String GAME_FIELD_MONSTERS_COUNT = "monsters_count";
    public static final String GAME_FIELD_CURSE_COUNT = "curse_count";
    public static final String GAME_FIELD_RUMORS_COUNT = "rumors_count";
    public static final String GAME_FIELD_CLUES_COUNT = "clues_count";
    public static final String GAME_FIELD_BLESSED_COUNT = "blessed_count";
    public static final String GAME_FIELD_DOOM_COUNT = "doom_count";
    public static final String GAME_FIELD_SCORE = "score";

    @DatabaseField(generatedId = true, columnName = GAME_FIELD_ID)
    public int id;

    @DatabaseField(dataType = DataType.STRING, columnName = GAME_FIELD_DATE)
    public String date;

    @DatabaseField(dataType = DataType.INTEGER, columnName = GAME_FIELD_ANCIENT_ONE_ID)
    public int ancientOneID;

    @DatabaseField(dataType = DataType.INTEGER, columnName = GAME_FIELD_PLAYERS_COUNT)
    public int playersCount;

    @DatabaseField(dataType = DataType.BOOLEAN, columnName = GAME_FIELD_SIMPLE_MYTHS)
    public boolean isSimpleMyths;

    @DatabaseField(dataType = DataType.BOOLEAN, columnName = GAME_FIELD_NORMAL_MYTHS)
    public boolean isNormalMyths;

    @DatabaseField(dataType = DataType.BOOLEAN, columnName = GAME_FIELD_HARD_MYTHS)
    public boolean isHardMyths;

    @DatabaseField(dataType = DataType.BOOLEAN, columnName = GAME_FIELD_STARTING_RUMOR)
    public boolean isStartingRumor;

    @DatabaseField(dataType = DataType.INTEGER, columnName = GAME_FIELD_GATES_COUNT)
    public int gatesCount = 0;

    @DatabaseField(dataType = DataType.INTEGER, columnName = GAME_FIELD_MONSTERS_COUNT)
    public int monstersCount = 0;

    @DatabaseField(dataType = DataType.INTEGER, columnName = GAME_FIELD_CURSE_COUNT)
    public int curseCount = 0;

    @DatabaseField(dataType = DataType.INTEGER, columnName = GAME_FIELD_RUMORS_COUNT)
    public int rumorsCount = 0;

    @DatabaseField(dataType = DataType.INTEGER, columnName = GAME_FIELD_CLUES_COUNT)
    public int cluesCount = 0;

    @DatabaseField(dataType = DataType.INTEGER, columnName = GAME_FIELD_BLESSED_COUNT)
    public int blessedCount = 0;

    @DatabaseField(dataType = DataType.INTEGER, columnName = GAME_FIELD_DOOM_COUNT)
    public int doomCount = 0;

    @DatabaseField(dataType = DataType.INTEGER, columnName = GAME_FIELD_SCORE)
    public int score = 0;

    public List<Investigator> invList  = new ArrayList<>();

    public Game() {
        id = -1;
    }

    protected Game(Parcel in) {
        id = in.readInt();
        date = in.readString();
        ancientOneID = in.readInt();
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
        invList = in.createTypedArrayList(Investigator.CREATOR);
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(date);
        parcel.writeInt(ancientOneID);
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
        parcel.writeTypedList(invList);
    }
}
