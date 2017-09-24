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
    private static final int DATABASE_VERSION = 3;

    //ссылки на DAO соответсвующие сущностям, хранимым в БД
    private InvestigatorLocalDAO investigatorLocalDAO = null;
    private AncientOneDAO ancientOneDAO = null;
    private static DatabaseStaticHelper helper = null;
    private Context context;

    public DatabaseStaticHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        new LocalDBAssetHelper(context).getWritableDatabase();
    }

    public static synchronized DatabaseStaticHelper getHelper(Context context) {
        if (helper == null) {
            helper = new DatabaseStaticHelper(context);
        }
        return helper;
    }

    //Выполняется, когда файл с БД не найден на устройстве
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
//        new LocalDBAssetHelper(context).getReadableDatabase();
    }

    //Выполняется, когда БД имеет версию отличную от текущей
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

        System.out.println("Update DB finish ");


    }

    //синглтон для InvestigatorDAO
    public InvestigatorLocalDAO getInvestigatorLocalDAO() throws SQLException{
        if(investigatorLocalDAO == null){
            investigatorLocalDAO = new InvestigatorLocalDAO(getConnectionSource(), InvestigatorLocal.class);
        }
        return investigatorLocalDAO;
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
        investigatorLocalDAO = null;
        ancientOneDAO = null;
    }
}
