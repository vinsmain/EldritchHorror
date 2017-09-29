package ru.mgusev.eldritchhorror;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvestigatorDAO extends BaseDaoImpl {
    protected InvestigatorDAO(ConnectionSource connectionSource, Class dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<Investigator> getAllInvestigatorsLocal() throws SQLException{
        return this.queryForAll();
    }

    public List<Investigator> getInvestigatorsListByGameID(int gameID) throws SQLException {
        QueryBuilder<Investigator, Integer> qb = this.queryBuilder();
        qb.where().eq(Investigator.INVESTIGATOR_FIELD_GAME_ID, gameID);
        return qb.query();
    }

    public void deleteInvestigatorsByGameID(int id) throws SQLException {
        DeleteBuilder<Investigator, Integer> db = this.deleteBuilder();
        db.where().eq(Investigator.INVESTIGATOR_FIELD_GAME_ID, id);
        db.delete();
    }
}
