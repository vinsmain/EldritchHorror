package ru.mgusev.eldritchhorror;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "eldritchHorrorDB";
    public static final String TABLE_GAMES = "games";

    public static final String KEY_ID = "_id";
    public static final String KEY_DATE = "date";
    public static final String KEY_ANCIENT_ONE = "ancient_one";
    public static final String KEY_PLAYERS_COUNT = "players_count";
    public static final String KEY_SIMPLE_MYTHS = "simple_myths";
    public static final String KEY_NORMAL_MYTHS = "normal_myths";
    public static final String KEY_HARD_MYTHS = "hard_myths";
    public static final String KEY_STARTING_RUMOR = "starting_rumor";
    public static final String KEY_GATES_COUNT = "gates_count";
    public static final String KEY_MONSTERS_COUNT = "monsters_count";
    public static final String KEY_CURSE_COUNT = "curse_count";
    public static final String KEY_RUMORS_COUNT = "rumors_count";
    public static final String KEY_CLUES_COUNT = "clues_count";
    public static final String KEY_BLESSED_COUNT = "blessed_count";
    public static final String KEY_DOOM_COUNT = "doom_count";
    public static final String KEY_SCORE = "score";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_GAMES + " (" + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_DATE + " TEXT, " + KEY_ANCIENT_ONE + " TEXT, " + KEY_PLAYERS_COUNT + " INTEGER, "
                + KEY_SIMPLE_MYTHS + " INTEGER, " + KEY_NORMAL_MYTHS + " INTEGER, " + KEY_HARD_MYTHS + " INTEGER, "
                + KEY_STARTING_RUMOR + " INTEGER, " + KEY_GATES_COUNT + " INTEGER, " + KEY_MONSTERS_COUNT + " INTEGER, "
                + KEY_CURSE_COUNT + " INTEGER, " + KEY_RUMORS_COUNT + " INTEGER, " + KEY_CLUES_COUNT + " INTEGER, "
                + KEY_BLESSED_COUNT + " INTEGER, " + KEY_DOOM_COUNT + " INTEGER, " + KEY_SCORE + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMES);
        onCreate(sqLiteDatabase);
    }
}
