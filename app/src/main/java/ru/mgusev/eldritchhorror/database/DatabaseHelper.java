package ru.mgusev.eldritchhorror.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.mgusev.eldritchhorror.model.Investigator;
import ru.mgusev.eldritchhorror.dao.GameDAO;
import ru.mgusev.eldritchhorror.dao.InvestigatorDAO;
import ru.mgusev.eldritchhorror.model.Game;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();

    //имя файла базы данных который будет храниться в /data/data/APPNAME/DATABASE_NAME.db
    private static final String DATABASE_NAME ="eldritchHorrorDB.db";

    //с каждым увеличением версии, при нахождении в устройстве БД с предыдущей версией будет выполнен метод onUpgrade();
    private static final int DATABASE_VERSION = 12;

    //ссылки на DAO соответсвующие сущностям, хранимым в БД
    private GameDAO gameDAO = null;
    private InvestigatorDAO investigatorDAO = null;
    private static DatabaseHelper helper = null;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getHelper(Context context) {
        if (helper == null) {
            helper = new DatabaseHelper(context);
        }
        return helper;
    }

    //Выполняется, когда файл с БД не найден на устройстве
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Game.class);
            TableUtils.createTable(connectionSource, Investigator.class);
            Log.e(TAG, "Create DB");
        } catch (SQLException e){
            Log.e(TAG, "Error creating DB " + DATABASE_NAME);
            throw new RuntimeException(e);
        }
    }

    //Выполняется, когда БД имеет версию отличную от текущей
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            if (oldVersion < 2) {
                Log.e(TAG, "Update 1 - 2");
                helper.getGameDAO().executeRaw("ALTER TABLE '" + Game.GAME_TABLE_NAME + "' ADD COLUMN " + Game.GAME_FIELD_PRELUDE_ID + " INTEGER DEFAULT 0;");
                helper.getGameDAO().executeRaw("ALTER TABLE '" + Game.GAME_TABLE_NAME + "' ADD COLUMN " + Game.GAME_FIELD_SOLVED_MYSTERIES_COUNT + " INTEGER DEFAULT 3;");
                Log.e(TAG, "Finish update 1 - 2");
            }

            if (oldVersion < 3) {
                Log.e(TAG, "Update 2 - 3");
                String queryGamesUpgrade = "PRAGMA foreign_keys = 0;\n" +
                        "\n" +
                        "CREATE TABLE games_temp_table AS SELECT *\n" +
                        "                                          FROM games;\n" +
                        "\n" +
                        "DROP TABLE games;\n" +
                        "\n" +
                        "CREATE TABLE games (\n" +
                        "    ancient_one_id                 INTEGER,\n" +
                        "    blessed_count                  INTEGER,\n" +
                        "    clues_count                    INTEGER,\n" +
                        "    curse_count                    INTEGER,\n" +
                        "    date                           VARCHAR,\n" +
                        "    doom_count                     INTEGER,\n" +
                        "    gates_count                    INTEGER,\n" +
                        "    _id                            BIGINT     PRIMARY KEY,\n" +
                        "    defeat_by_awakened_ancient_one SMALLINT,\n" +
                        "    defeat_by_elimination          SMALLINT,\n" +
                        "    defeat_by_mythos_depletion     SMALLINT,\n" +
                        "    hard_myths                     SMALLINT,\n" +
                        "    normal_myths                   SMALLINT,\n" +
                        "    simple_myths                   SMALLINT,\n" +
                        "    starting_rumor                 SMALLINT,\n" +
                        "    win_game                       SMALLINT,\n" +
                        "    monsters_count                 INTEGER,\n" +
                        "    players_count                  INTEGER,\n" +
                        "    prelude_id                     INTEGER,\n" +
                        "    rumors_count                   INTEGER,\n" +
                        "    score                          INTEGER,\n" +
                        "    solved_mysteries_count         INTEGER\n" +
                        ");\n" +
                        "\n" +
                        "INSERT INTO games (\n" +
                        "                      ancient_one_id,\n" +
                        "                      blessed_count,\n" +
                        "                      clues_count,\n" +
                        "                      curse_count,\n" +
                        "                      date,\n" +
                        "                      doom_count,\n" +
                        "                      gates_count,\n" +
                        "                      _id,\n" +
                        "                      defeat_by_awakened_ancient_one,\n" +
                        "                      defeat_by_elimination,\n" +
                        "                      defeat_by_mythos_depletion,\n" +
                        "                      hard_myths,\n" +
                        "                      normal_myths,\n" +
                        "                      simple_myths,\n" +
                        "                      starting_rumor,\n" +
                        "                      win_game,\n" +
                        "                      monsters_count,\n" +
                        "                      players_count,\n" +
                        "                      prelude_id,\n" +
                        "                      rumors_count,\n" +
                        "                      score,\n" +
                        "                      solved_mysteries_count\n" +
                        "                  )\n" +
                        "                  SELECT ancient_one_id,\n" +
                        "                         blessed_count,\n" +
                        "                         clues_count,\n" +
                        "                         curse_count,\n" +
                        "                         date,\n" +
                        "                         doom_count,\n" +
                        "                         gates_count,\n" +
                        "                         _id,\n" +
                        "                         defeat_by_awakened_ancient_one,\n" +
                        "                         defeat_by_elimination,\n" +
                        "                         defeat_by_mythos_depletion,\n" +
                        "                         hard_myths,\n" +
                        "                         normal_myths,\n" +
                        "                         simple_myths,\n" +
                        "                         starting_rumor,\n" +
                        "                         win_game,\n" +
                        "                         monsters_count,\n" +
                        "                         players_count,\n" +
                        "                         prelude_id,\n" +
                        "                         rumors_count,\n" +
                        "                         score,\n" +
                        "                         solved_mysteries_count\n" +
                        "                    FROM games_temp_table;\n" +
                        "\n" +
                        "DROP TABLE games_temp_table;\n" +
                        "\n" +
                        "PRAGMA foreign_keys = 1;";

                String queryInvUpgrade = "PRAGMA foreign_keys = 0;\n" +
                        "\n" +
                        "CREATE TABLE investigators_temp_table AS SELECT *\n" +
                        "                                          FROM investigators;\n" +
                        "\n" +
                        "DROP TABLE investigators;\n" +
                        "\n" +
                        "CREATE TABLE investigators (\n" +
                        "    _id            BIGINT PRIMARY KEY,\n" +
                        "    game_id        BIGINT,\n" +
                        "    image_resource STRING,\n" +
                        "    is_male        BOOLEAN,\n" +
                        "    name_en        STRING,\n" +
                        "    name_ru        STRING,\n" +
                        "    occupation_en  STRING,\n" +
                        "    occupation_ru  STRING,\n" +
                        "    is_starting    BOOLEAN,\n" +
                        "    is_replacement BOOLEAN,\n" +
                        "    is_dead        BOOLEAN,\n" +
                        "    expansion_id   INTEGER\n" +
                        ");\n" +
                        "\n" +
                        "INSERT INTO investigators (\n" +
                        "                              _id,\n" +
                        "                              game_id,\n" +
                        "                              image_resource,\n" +
                        "                              is_male,\n" +
                        "                              name_en,\n" +
                        "                              name_ru,\n" +
                        "                              occupation_en,\n" +
                        "                              occupation_ru,\n" +
                        "                              is_starting,\n" +
                        "                              is_replacement,\n" +
                        "                              is_dead,\n" +
                        "                              expansion_id\n" +
                        "                          )\n" +
                        "                          SELECT _id,\n" +
                        "                                 game_id,\n" +
                        "                                 image_resource,\n" +
                        "                                 is_male,\n" +
                        "                                 name_en,\n" +
                        "                                 name_ru,\n" +
                        "                                 occupation_en,\n" +
                        "                                 occupation_ru,\n" +
                        "                                 is_starting,\n" +
                        "                                 is_replacement,\n" +
                        "                                 is_dead,\n" +
                        "                                 expansion_id\n" +
                        "                            FROM investigators_temp_table;\n" +
                        "\n" +
                        "DROP TABLE investigators_temp_table;\n" +
                        "\n" +
                        "PRAGMA foreign_keys = 1;";

                helper.getGameDAO().queryRaw(queryGamesUpgrade);
                helper.getInvestigatorDAO().queryRaw(queryInvUpgrade);
                Log.e(TAG, "Finish update 2 - 3");
            }

            if (oldVersion < 4) {
                Log.e(TAG, "Update 3 - 4");
                helper.getGameDAO().executeRaw("ALTER TABLE '" + Game.GAME_TABLE_NAME + "' ADD COLUMN " + Game.GAME_FIELD_USER_ID + " STRING DEFAULT null;");
                Log.e(TAG, "Finish update 3 - 4");
            }

            if (oldVersion < 7) {
                Log.e(TAG, "Update 4 - 7");
                helper.getGameDAO().executeRaw("ALTER TABLE '" + Game.GAME_TABLE_NAME + "' ADD COLUMN " + Game.GAME_FIELD_LAST_MODIFIED + " BIGINT DEFAULT 0;");
                Date currentDate = new Date();
                helper.getGameDAO().executeRaw("UPDATE '" + Game.GAME_TABLE_NAME + "' SET " + Game.GAME_FIELD_LAST_MODIFIED + " = " + currentDate.getTime() + ";");
                Log.e(TAG, "Finish update 4 - 7");
            }

            if (oldVersion < 8) {
                Log.e(TAG, "Update 7 - 8");
                String queryGamesUpgradeV8 = "PRAGMA foreign_keys = 0;\n" +
                        "\n" +
                        "CREATE TABLE games_temp_table AS SELECT *\n" +
                        "                                          FROM games;\n" +
                        "\n" +
                        "DROP TABLE games;\n" +
                        "\n" +
                        "CREATE TABLE games (\n" +
                        "    ancient_one_id                 INTEGER,\n" +
                        "    blessed_count                  INTEGER,\n" +
                        "    clues_count                    INTEGER,\n" +
                        "    curse_count                    INTEGER,\n" +
                        "    date                           BIGINT,\n" +
                        "    doom_count                     INTEGER,\n" +
                        "    gates_count                    INTEGER,\n" +
                        "    _id                            BIGINT     PRIMARY KEY,\n" +
                        "    defeat_by_awakened_ancient_one SMALLINT,\n" +
                        "    defeat_by_elimination          SMALLINT,\n" +
                        "    defeat_by_mythos_depletion     SMALLINT,\n" +
                        "    hard_myths                     SMALLINT,\n" +
                        "    normal_myths                   SMALLINT,\n" +
                        "    simple_myths                   SMALLINT,\n" +
                        "    starting_rumor                 SMALLINT,\n" +
                        "    win_game                       SMALLINT,\n" +
                        "    monsters_count                 INTEGER,\n" +
                        "    players_count                  INTEGER,\n" +
                        "    prelude_id                     INTEGER,\n" +
                        "    rumors_count                   INTEGER,\n" +
                        "    score                          INTEGER,\n" +
                        "    solved_mysteries_count         INTEGER\n" +
                        ");\n" +
                        "\n" +
                        "INSERT INTO games (\n" +
                        "                      ancient_one_id,\n" +
                        "                      blessed_count,\n" +
                        "                      clues_count,\n" +
                        "                      curse_count,\n" +
                        "                      date,\n" +
                        "                      doom_count,\n" +
                        "                      gates_count,\n" +
                        "                      _id,\n" +
                        "                      defeat_by_awakened_ancient_one,\n" +
                        "                      defeat_by_elimination,\n" +
                        "                      defeat_by_mythos_depletion,\n" +
                        "                      hard_myths,\n" +
                        "                      normal_myths,\n" +
                        "                      simple_myths,\n" +
                        "                      starting_rumor,\n" +
                        "                      win_game,\n" +
                        "                      monsters_count,\n" +
                        "                      players_count,\n" +
                        "                      prelude_id,\n" +
                        "                      rumors_count,\n" +
                        "                      score,\n" +
                        "                      solved_mysteries_count\n" +
                        "                  )\n" +
                        "                  SELECT ancient_one_id,\n" +
                        "                         blessed_count,\n" +
                        "                         clues_count,\n" +
                        "                         curse_count,\n" +
                        "                         date,\n" +
                        "                         doom_count,\n" +
                        "                         gates_count,\n" +
                        "                         _id,\n" +
                        "                         defeat_by_awakened_ancient_one,\n" +
                        "                         defeat_by_elimination,\n" +
                        "                         defeat_by_mythos_depletion,\n" +
                        "                         hard_myths,\n" +
                        "                         normal_myths,\n" +
                        "                         simple_myths,\n" +
                        "                         starting_rumor,\n" +
                        "                         win_game,\n" +
                        "                         monsters_count,\n" +
                        "                         players_count,\n" +
                        "                         prelude_id,\n" +
                        "                         rumors_count,\n" +
                        "                         score,\n" +
                        "                         solved_mysteries_count\n" +
                        "                    FROM games_temp_table;\n" +
                        "\n" +
                        "DROP TABLE games_temp_table;\n" +
                        "\n" +
                        "PRAGMA foreign_keys = 1;";

                // return the orders with the sum of their amounts per account
                GenericRawResults<String[]> rawResults = helper.getGameDAO().queryRaw("SELECT " + Game.GAME_FIELD_ID + ", " + Game.GAME_FIELD_DATE + " FROM '" + Game.GAME_TABLE_NAME + "';");
                // page through the results
                long date;
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                for (String[] resultArray : rawResults) {
                    try {
                        helper.getGameDAO().queryRaw("UPDATE '" + Game.GAME_TABLE_NAME + "' SET " + Game.GAME_FIELD_DATE + " = " + formatter.parse(resultArray[1]).getTime() + " WHERE " + Game.GAME_FIELD_ID + " = " + resultArray[0] + ";");

                        date = (new Date()).getTime();
                        helper.getInvestigatorDAO().queryRaw("UPDATE '" + Investigator.INVESTIGATOR_TABLE_NAME + "' SET " + Investigator.INVESTIGATOR_FIELD_GAME_ID + " = " + date + " WHERE " + Investigator.INVESTIGATOR_FIELD_GAME_ID + " = " + resultArray[0] + ";");
                        helper.getGameDAO().queryRaw("UPDATE '" + Game.GAME_TABLE_NAME + "' SET " + Game.GAME_FIELD_ID + " = " + date + " WHERE " + Game.GAME_FIELD_ID + " = " + resultArray[0] + ";");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    rawResults.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                helper.getGameDAO().queryRaw(queryGamesUpgradeV8);
                Date currentDate = new Date();
                helper.getGameDAO().executeRaw("UPDATE '" + Game.GAME_TABLE_NAME + "' SET " + Game.GAME_FIELD_LAST_MODIFIED + " = " + currentDate.getTime() + ";");
                Log.e(TAG, "Finish update 7 - 8");
            }

            if (oldVersion < 9) {
                Log.e(TAG, "Update 8 - 9");
                helper.getGameDAO().executeRaw("ALTER TABLE '" + Game.GAME_TABLE_NAME + "' ADD COLUMN " + Game.GAME_FIELD_ADVENTURE_ID + " INTEGER DEFAULT 0;");
                helper.getInvestigatorDAO().executeRaw("ALTER TABLE '" + Investigator.INVESTIGATOR_TABLE_NAME + "' ADD COLUMN " + Investigator.INVESTIGATOR_FIELD_SPECIALIZATION_ID + " INTEGER DEFAULT 0;");
                List<Investigator> invList = HelperFactory.getStaticHelper().getInvestigatorDAO().getAllInvestigatorsLocal();
                for (Investigator investigator : invList) {
                    helper.getInvestigatorDAO().executeRaw("UPDATE '" + Investigator.INVESTIGATOR_TABLE_NAME + "' SET " + Investigator.INVESTIGATOR_FIELD_SPECIALIZATION_ID + " = " + investigator.specialization +
                            " WHERE " + Investigator.INVESTIGATOR_FIELD_ID + " = " + investigator.id + ";");
                }
                Log.e(TAG, "Finish update 8 - 9");
            }

            if (oldVersion < 12) {
                Log.e(TAG, "Update 9 - 12");
                List<Game> gameList = helper.getGameDAO().getGamesSortDateUp();
                for (Game game : gameList) {
                    if (!game.isDefeatByAwakenedAncientOne && !game.isDefeatByMythosDepletion && !game.isDefeatByElimination) {
                        game.isDefeatByAwakenedAncientOne = true;
                        game.invList = HelperFactory.getHelper().getInvestigatorDAO().getInvestigatorsListByGameID(game.id);
                        game.lastModified = (new Date()).getTime();
                        game.userID = null;

                        //FirebaseHelper.addGame(game);
                        HelperFactory.getHelper().getGameDAO().writeGameToDB(game);
                    }
                }
                Log.e(TAG, "Finish update 9 - 12");
            }

            Log.e(TAG, "Update DB");
        } catch (SQLException e){
            Log.e(TAG, "Error upgrading db " + DATABASE_NAME + " from ver " + oldVersion);
            throw new RuntimeException(e);
        }
    }

    //синглтон для GameDAO
    public GameDAO getGameDAO() throws SQLException{
        if(gameDAO == null){
            gameDAO = new GameDAO(getConnectionSource(), Game.class);
        }
        return gameDAO;
    }

    //синглтон для InvestigatorDAO
    public InvestigatorDAO getInvestigatorDAO() throws SQLException{
        if(investigatorDAO == null){
            investigatorDAO = new InvestigatorDAO(getConnectionSource(), Investigator.class);
        }
        return investigatorDAO;
    }

    //выполняется при закрытии приложения
    @Override
    public void close(){
        super.close();
        gameDAO = null;
        investigatorDAO = null;
    }
}