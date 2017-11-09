package ru.mgusev.eldritchhorror.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import ru.mgusev.eldritchhorror.database.HelperFactory;
import ru.mgusev.eldritchhorror.model.Expansion;
import ru.mgusev.eldritchhorror.model.Game;

public class ExpansionDAO extends BaseDaoImpl {

    public ExpansionDAO(ConnectionSource connectionSource, Class dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public String getImageResourceByAncientOneID(int ancientOneID) throws SQLException {
        int expansionID = HelperFactory.getStaticHelper().getAncientOneDAO().getExpansionID(ancientOneID);
        QueryBuilder<Expansion, Integer> qb = this.queryBuilder();
        qb.where().eq(Expansion.EXPANSION_FIELD_ID, expansionID);
        return qb.queryForFirst().imageResource;
    }

    public String getImageResourceByID(int expansionID) throws SQLException {
        QueryBuilder<Expansion, Integer> qb = this.queryBuilder();
        qb.where().eq(Expansion.EXPANSION_FIELD_ID, expansionID);
        return qb.queryForFirst().imageResource;
    }

    public List<Expansion> getAllExpansion() throws SQLException{
        QueryBuilder<Expansion, Integer> qb = this.queryBuilder();
        qb.where().isNotNull(Expansion.EXPANSION_FIELD_IMAGE_RESOURCE);
        return qb.query();
    }

    public boolean isEnableByID(int expansionID) throws SQLException {
        QueryBuilder<Expansion, Integer> qb = this.queryBuilder();
        qb.where().eq(Expansion.EXPANSION_FIELD_ID, expansionID);
        return qb.queryForFirst().isEnable;
    }

    public void writeExpansionToDB(Expansion expansion) throws SQLException {
        this.createOrUpdate(expansion);
    }
}
