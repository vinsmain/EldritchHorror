package ru.mgusev.eldritchhorror;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseStaticHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();

    //имя файла базы данных который будет храниться в /data/data/APPNAME/DATABASE_NAME.db
    private static final String DATABASE_NAME ="EHLocalDB.db";

    //с каждым увеличением версии, при нахождении в устройстве БД с предыдущей версией будет выполнен метод onUpgrade();
    private static final int DATABASE_VERSION = 1;

    //ссылки на DAO соответсвующие сущностям, хранимым в БД
    private InvestigatorDAO investigatorDAO = null;
    private AncientOneDAO ancientOneDAO = null;
    private static DatabaseStaticHelper helper = null;
    private Context context;

    public DatabaseStaticHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        new LocalDBAssetHelper(context).getReadableDatabase();
    }

    public static synchronized DatabaseStaticHelper getHelper(Context context) {
        if (helper == null) {
            helper = new DatabaseStaticHelper(context);
        }
        //usageCounter.incrementAndGet();
        return helper;
    }

    //Выполняется, когда файл с БД не найден на устройстве
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        System.out.println("Start copy localDB");
        //new LocalDBAssetHelper(context).getReadableDatabase();
        System.out.println("Finish copy localDB");
        //TableUtils.createTable(connectionSource, Game.class);
        //TableUtils.createTable(connectionSource, Investigator.class);
    }

    //Выполняется, когда БД имеет версию отличную от текущей
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        /*try {
            TableUtils.dropTable(connectionSource, Game.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e){
            Log.e(TAG, "Error upgrading db " + DATABASE_NAME + " from ver "+oldVersion);
            throw new RuntimeException(e);
        }*/
    }

    //синглтон для InvestigatorDAO
    public InvestigatorDAO getInvestigatorDAO() throws SQLException{
        if(investigatorDAO == null){
            investigatorDAO = new InvestigatorDAO(getConnectionSource(), Investigator.class);
        }
        return investigatorDAO;
    }

    //синглтон для AncientOneDAO
    public AncientOneDAO getAncientOneDAO() throws SQLException{
        if(ancientOneDAO == null){
            ancientOneDAO = new AncientOneDAO(getConnectionSource(), AncientOne.class);
        }
        return ancientOneDAO;
    }

    //выполняется при закрытии приложения
    @Override
    public void close(){
        super.close();
        investigatorDAO = null;
        ancientOneDAO = null;
    }
}
