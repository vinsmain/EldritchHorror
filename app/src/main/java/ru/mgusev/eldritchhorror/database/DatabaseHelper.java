package ru.mgusev.eldritchhorror.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;

import ru.mgusev.eldritchhorror.model.Investigator;
import ru.mgusev.eldritchhorror.dao.GameDAO;
import ru.mgusev.eldritchhorror.dao.InvestigatorDAO;
import ru.mgusev.eldritchhorror.model.Game;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();

    //имя файла базы данных который будет храниться в /data/data/APPNAME/DATABASE_NAME.db
    private static final String DATABASE_NAME ="eldritchHorrorDB.db";

    //с каждым увеличением версии, при нахождении в устройстве БД с предыдущей версией будет выполнен метод onUpgrade();
    private static final int DATABASE_VERSION = 1;

    //ссылки на DAO соответсвующие сущностям, хранимым в БД
    private GameDAO gameDAO = null;
    private InvestigatorDAO investigatorDAO = null;
    private static DatabaseHelper helper = null;
    private Context context;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
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
            System.out.println("Create DB");
        } catch (SQLException e){
            Log.e(TAG, "Error creating DB " + DATABASE_NAME);
            throw new RuntimeException(e);
        }
    }

    //Выполняется, когда БД имеет версию отличную от текущей
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Investigator.class, true);
            TableUtils.createTable(connectionSource, Investigator.class);
            System.out.println("Update DB");
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