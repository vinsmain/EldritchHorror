package ru.mgusev.eldritchhorror.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import ru.mgusev.eldritchhorror.model.Investigator;

public class InvestigatorDAO extends BaseDaoImpl {
    public InvestigatorDAO(ConnectionSource connectionSource, Class dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<Investigator> getAllInvestigatorsLocal() throws SQLException{
        QueryBuilder<Investigator, Integer> qb = this.queryBuilder();
        qb.orderBy(Investigator.INVESTIGATOR_FIELD_EXPANSION_ID, true);
        qb.orderBy(Investigator.INVESTIGATOR_FIELD_NAME, true);
        return qb.query();
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