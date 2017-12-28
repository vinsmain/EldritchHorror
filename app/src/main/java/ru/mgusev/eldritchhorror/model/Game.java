package ru.mgusev.eldritchhorror.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@DatabaseTable(tableName = "games")
public class Game implements Parcelable {

    public static final String GAME_TABLE_NAME = "games";

    public static final String GAME_FIELD_ID = "_id";
    public static final String GAME_FIELD_DATE = "date";
    public static final String GAME_FIELD_ANCIENT_ONE_ID = "ancient_one_id";
    public static final String GAME_FIELD_PLAYERS_COUNT = "players_count";
    public static final String GAME_FIELD_SIMPLE_MYTHS = "simple_myths";
    public static final String GAME_FIELD_NORMAL_MYTHS = "normal_myths";
    public static final String GAME_FIELD_HARD_MYTHS = "hard_myths";
    public static final String GAME_FIELD_STARTING_RUMOR = "starting_rumor";
    public static final String GAME_FIELD_WIN_GAME = "win_game";
    public static final String GAME_FIELD_DEFEAT_BY_ELIMINATION = "defeat_by_elimination";
    public static final String GAME_FIELD_DEFEAT_BY_MYTHOS_DEPLETION = "defeat_by_mythos_depletion";
    public static final String GAME_FIELD_DEFEAT_BY_AWAKENED_ANCIENT_ONE = "defeat_by_awakened_ancient_one";
    public static final String GAME_FIELD_GATES_COUNT = "gates_count";
    public static final String GAME_FIELD_MONSTERS_COUNT = "monsters_count";
    public static final String GAME_FIELD_CURSE_COUNT = "curse_count";
    public static final String GAME_FIELD_RUMORS_COUNT = "rumors_count";
    public static final String GAME_FIELD_CLUES_COUNT = "clues_count";
    public static final String GAME_FIELD_BLESSED_COUNT = "blessed_count";
    public static final String GAME_FIELD_DOOM_COUNT = "doom_count";
    public static final String GAME_FIELD_SCORE = "score";
    public static final String GAME_FIELD_PRELUDE_ID = "prelude_id";
    public static final String GAME_FIELD_SOLVED_MYSTERIES_COUNT = "solved_mysteries_count";

    @DatabaseField(id = true, dataType = DataType.LONG, columnName = GAME_FIELD_ID)
    public long id;

    @DatabaseField(dataType = DataType.DATE_STRING, columnName = GAME_FIELD_DATE)
    public Date date;

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

    @DatabaseField(dataType = DataType.BOOLEAN, columnName = GAME_FIELD_WIN_GAME)
    public boolean isWinGame;

    @DatabaseField(dataType = DataType.BOOLEAN, columnName = GAME_FIELD_DEFEAT_BY_ELIMINATION)
    public boolean isDefeatByElimination;

    @DatabaseField(dataType = DataType.BOOLEAN, columnName = GAME_FIELD_DEFEAT_BY_MYTHOS_DEPLETION)
    public boolean isDefeatByMythosDepletion;

    @DatabaseField(dataType = DataType.BOOLEAN, columnName = GAME_FIELD_DEFEAT_BY_AWAKENED_ANCIENT_ONE)
    public boolean isDefeatByAwakenedAncientOne;

    @DatabaseField(dataType = DataType.INTEGER, columnName = GAME_FIELD_GATES_COUNT)
    public int gatesCount;

    @DatabaseField(dataType = DataType.INTEGER, columnName = GAME_FIELD_MONSTERS_COUNT)
    public int monstersCount;

    @DatabaseField(dataType = DataType.INTEGER, columnName = GAME_FIELD_CURSE_COUNT)
    public int curseCount;

    @DatabaseField(dataType = DataType.INTEGER, columnName = GAME_FIELD_RUMORS_COUNT)
    public int rumorsCount;

    @DatabaseField(dataType = DataType.INTEGER, columnName = GAME_FIELD_CLUES_COUNT)
    public int cluesCount;

    @DatabaseField(dataType = DataType.INTEGER, columnName = GAME_FIELD_BLESSED_COUNT)
    public int blessedCount;

    @DatabaseField(dataType = DataType.INTEGER, columnName = GAME_FIELD_DOOM_COUNT)
    public int doomCount;

    @DatabaseField(dataType = DataType.INTEGER, columnName = GAME_FIELD_SCORE)
    public int score;

    @DatabaseField(dataType = DataType.INTEGER, columnName = GAME_FIELD_PRELUDE_ID)
    public int preludeID;

    @DatabaseField(dataType = DataType.INTEGER, columnName = GAME_FIELD_SOLVED_MYSTERIES_COUNT)
    public int solvedMysteriesCount;

    public List<Investigator> invList;

    public Game() {
        id = -1;
        date = new Date();
        ancientOneID = -1;
        playersCount = 1;
        isSimpleMyths = true;
        isNormalMyths = true;
        isHardMyths = true;
        isStartingRumor = false;
        isWinGame = true;
        isDefeatByElimination = false;
        isDefeatByMythosDepletion = false;
        isDefeatByAwakenedAncientOne = true;
        gatesCount = 0;
        monstersCount = 0;
        curseCount = 0;
        rumorsCount = 0;
        cluesCount = 0;
        blessedCount = 0;
        doomCount = 0;
        score = 0;
        preludeID = 0;
        solvedMysteriesCount = 3;
        invList = new ArrayList<>();
    }

    protected Game(Parcel in) {
        id = in.readLong();
        date = new Date(in.readLong());
        ancientOneID = in.readInt();
        playersCount = in.readInt();
        isSimpleMyths = in.readByte() != 0;
        isNormalMyths = in.readByte() != 0;
        isHardMyths = in.readByte() != 0;
        isStartingRumor = in.readByte() != 0;
        isWinGame = in.readByte() != 0;
        isDefeatByElimination = in.readByte() != 0;
        isDefeatByMythosDepletion = in.readByte() != 0;
        isDefeatByAwakenedAncientOne = in.readByte() != 0;
        gatesCount = in.readInt();
        monstersCount = in.readInt();
        curseCount = in.readInt();
        rumorsCount = in.readInt();
        cluesCount = in.readInt();
        blessedCount = in.readInt();
        doomCount = in.readInt();
        score = in.readInt();
        preludeID = in.readInt();
        solvedMysteriesCount = in.readInt();
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
        parcel.writeLong(id);
        parcel.writeLong(date.getTime());
        parcel.writeInt(ancientOneID);
        parcel.writeInt(playersCount);
        parcel.writeByte((byte) (isSimpleMyths ? 1 : 0));
        parcel.writeByte((byte) (isNormalMyths ? 1 : 0));
        parcel.writeByte((byte) (isHardMyths ? 1 : 0));
        parcel.writeByte((byte) (isStartingRumor ? 1 : 0));
        parcel.writeByte((byte) (isWinGame ? 1 : 0));
        parcel.writeByte((byte) (isDefeatByElimination ? 1 : 0));
        parcel.writeByte((byte) (isDefeatByMythosDepletion ? 1 : 0));
        parcel.writeByte((byte) (isDefeatByAwakenedAncientOne ? 1 : 0));
        parcel.writeInt(gatesCount);
        parcel.writeInt(monstersCount);
        parcel.writeInt(curseCount);
        parcel.writeInt(rumorsCount);
        parcel.writeInt(cluesCount);
        parcel.writeInt(blessedCount);
        parcel.writeInt(doomCount);
        parcel.writeInt(score);
        parcel.writeInt(preludeID);
        parcel.writeInt(solvedMysteriesCount);
        parcel.writeTypedList(invList);
    }
}
