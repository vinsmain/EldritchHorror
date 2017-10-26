package ru.mgusev.eldritchhorror.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.List;

import ru.mgusev.eldritchhorror.model.AncientOne;

public class AncientOneDAO  extends BaseDaoImpl {
    public AncientOneDAO(ConnectionSource connectionSource, Class dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<AncientOne> getAllAncientOnes() throws SQLException{
        return this.queryForAll();
    }

    public String[] getAncientOneArray() throws SQLException {
        List<AncientOne> ancientOneArrayList = getAllAncientOnes();
        String[] ancientOneArray = new String[ancientOneArrayList.size()];
        for (int i = 0; i < ancientOneArrayList.size(); i++) {
            ancientOneArray[i] = ancientOneArrayList.get(i).name;
        }
        return ancientOneArray;
    }

    public int getAncientOneIDByName(String name) throws SQLException {
        QueryBuilder<AncientOne, Integer> qb = this.queryBuilder();
        qb.where().eq(AncientOne.ANCIENT_ONE_FIELD_NAME, name);
        return qb.queryForFirst().id;
    }

    public String getAncientOneNameByID(int id) throws SQLException {
        QueryBuilder<AncientOne, Integer> qb = this.queryBuilder();
        qb.where().eq(AncientOne.ANCIENT_ONE_FIELD_ID, id);
        return qb.queryForFirst().name;
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
}
