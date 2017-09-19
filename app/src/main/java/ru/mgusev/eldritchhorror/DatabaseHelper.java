package ru.mgusev.eldritchhorror;

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.support.constraint.solver.Goal;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();

    //имя файла базы данных который будет храниться в /data/data/APPNAME/DATABASE_NAME.db
    private static final String DATABASE_NAME ="eldritchHorrorDB.db";

    //с каждым увеличением версии, при нахождении в устройстве БД с предыдущей версией будет выполнен метод onUpgrade();
    private static final int DATABASE_VERSION = 1;

    //ссылки на DAO соответсвующие сущностям, хранимым в БД
    private GameDAO gameDAO = null;


    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Выполняется, когда файл с БД не найден на устройстве
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Game.class);
        } catch (SQLException e){
            Log.e(TAG, "Error creating DB " + DATABASE_NAME);
            throw new RuntimeException(e);
        }
    }

    //Выполняется, когда БД имеет версию отличную от текущей
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Game.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e){
            Log.e(TAG, "Error upgrading db " + DATABASE_NAME + " from ver "+oldVersion);
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

    //выполняется при закрытии приложения
    @Override
    public void close(){
        super.close();
        gameDAO = null;
    }
}
