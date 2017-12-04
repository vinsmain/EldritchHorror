package ru.mgusev.eldritchhorror.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.mgusev.eldritchhorror.database.HelperFactory;
import ru.mgusev.eldritchhorror.model.AncientOne;

public class AncientOneDAO  extends BaseDaoImpl {
    public AncientOneDAO(ConnectionSource connectionSource, Class dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<AncientOne> getAllAncientOnes() throws SQLException{
        return this.queryForAll();
    }

    public List<String> getAncientOneNameList() throws SQLException {
        QueryBuilder<AncientOne, Integer> qb = this.queryBuilder();
        if (Locale.getDefault().getLanguage().equals("ru")) qb.orderBy(AncientOne.ANCIENT_ONE_FIELD_NAME_RU, true);
        else qb.orderBy(AncientOne.ANCIENT_ONE_FIELD_NAME_EN, true);
        List<AncientOne> ancientOneList = qb.query();
        List<String> nameList = new ArrayList<>();
        for (AncientOne ancientOne : ancientOneList) {
            if (HelperFactory.getStaticHelper().getExpansionDAO().isEnableByID(ancientOne.expansionID)) nameList.add(ancientOne.getName());
        }
        return nameList;
    }

    public int getAncientOneIDByName(String name) throws SQLException {
        if (name.contains("'")) name = name.replace("'", "''");
        QueryBuilder<AncientOne, Integer> qb = this.queryBuilder();
        if (Locale.getDefault().getLanguage().equals("ru")) qb.where().eq(AncientOne.ANCIENT_ONE_FIELD_NAME_RU, name);
        else qb.where().eq(AncientOne.ANCIENT_ONE_FIELD_NAME_EN, name);
        return qb.queryForFirst().id;
    }

    public String getAncientOneNameByID(int id) throws SQLException {
        QueryBuilder<AncientOne, Integer> qb = this.queryBuilder();
        qb.where().eq(AncientOne.ANCIENT_ONE_FIELD_ID, id);
        return qb.queryForFirst().getName();
    }

    public String getAncientOneImageResourceByID(int id) throws SQLException {
        QueryBuilder<AncientOne, Integer> qb = this.queryBuilder();
        qb.where().eq(AncientOne.ANCIENT_ONE_FIELD_ID, id);
        return qb.queryForFirst().imageResource;
    }

    public int getExpansionID(int ancientOneID) throws SQLException {
        QueryBuilder<AncientOne, Integer> qb = this.queryBuilder();
        qb.where().eq(AncientOne.ANCIENT_ONE_FIELD_ID, ancientOneID);
        return qb.queryForFirst().expansionID;
    }

    public AncientOne getAncientOneByName(String name) throws SQLException {
        if (name.contains("'")) name = name.replace("'", "''");
        QueryBuilder<AncientOne, Integer> qb = this.queryBuilder();
        if (Locale.getDefault().getLanguage().equals("ru")) qb.where().eq(AncientOne.ANCIENT_ONE_FIELD_NAME_RU, name);
        else qb.where().eq(AncientOne.ANCIENT_ONE_FIELD_NAME_EN, name);
        return qb.queryForFirst();
    }
}
