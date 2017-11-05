package ru.mgusev.eldritchhorror.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import ru.mgusev.eldritchhorror.dao.ExpansionDAO;
import ru.mgusev.eldritchhorror.model.Expansion;
import ru.mgusev.eldritchhorror.model.Investigator;
import ru.mgusev.eldritchhorror.dao.AncientOneDAO;
import ru.mgusev.eldritchhorror.dao.InvestigatorDAO;
import ru.mgusev.eldritchhorror.model.AncientOne;

public class DatabaseLocalHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();

    //имя файла базы данных который будет храниться в /data/data/APPNAME/DATABASE_NAME.db
    private static final String DATABASE_NAME ="EHLocalDB.db";

    //с каждым увеличением версии, при нахождении в устройстве БД с предыдущей версией будет выполнен метод onUpgrade();
    private static final int DATABASE_VERSION = 6;

    //ссылки на DAO соответсвующие сущностям, хранимым в БД
    private InvestigatorDAO investigatorDAO = null;
    private AncientOneDAO ancientOneDAO = null;
    private ExpansionDAO expansionDAO = null;
    private static DatabaseLocalHelper helper = null;
    private Context context;

    public DatabaseLocalHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        new LocalDBAssetHelper(context).getWritableDatabase();
    }

    public static synchronized DatabaseLocalHelper getHelper(Context context) {
        if (helper == null) {
            helper = new DatabaseLocalHelper(context);
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

    //синглтон для ExpansionDAO
    public ExpansionDAO getExpansionDAO() throws SQLException{
        if(expansionDAO == null){
            expansionDAO = new ExpansionDAO(getConnectionSource(), Expansion.class);
        }
        return expansionDAO;
    }

    //выполняется при закрытии приложения
    @Override
    public void close(){
        super.close();
        investigatorDAO = null;
        ancientOneDAO = null;
        expansionDAO = null;
    }
}
