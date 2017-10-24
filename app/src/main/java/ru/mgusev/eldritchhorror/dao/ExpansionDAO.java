package ru.mgusev.eldritchhorror.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import ru.mgusev.eldritchhorror.database.HelperFactory;
import ru.mgusev.eldritchhorror.model.AncientOne;
import ru.mgusev.eldritchhorror.model.Expansion;

public class ExpansionDAO extends BaseDaoImpl {

    public ExpansionDAO(ConnectionSource connectionSource, Class dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public String getImageResourceByAncientOne(int ancientOneID) throws SQLException {
        int expansionID = HelperFactory.getStaticHelper().getAncientOneDAO().getExpansionImageResource(ancientOneID);
        QueryBuilder<Expansion, Integer> qb = this.queryBuilder();
        qb.where().eq(Expansion.EXPANSION_FIELD_ID, expansionID);
        return qb.queryForFirst().imageResource;
    }
}
