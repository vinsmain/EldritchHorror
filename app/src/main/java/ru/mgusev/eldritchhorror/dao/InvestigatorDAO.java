package ru.mgusev.eldritchhorror.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ru.mgusev.eldritchhorror.database.HelperFactory;
import ru.mgusev.eldritchhorror.model.Game;
import ru.mgusev.eldritchhorror.model.Investigator;
import ru.mgusev.eldritchhorror.model.Localization;

public class InvestigatorDAO extends BaseDaoImpl {

    public InvestigatorDAO(ConnectionSource connectionSource, Class dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<Investigator> getAllInvestigatorsLocal() throws SQLException{
        QueryBuilder<Investigator, Integer> qb = this.queryBuilder();
        qb.orderBy(Investigator.INVESTIGATOR_FIELD_EXPANSION_ID, true);
        if (Localization.getInstance().isRusLocale()) qb.orderBy(Investigator.INVESTIGATOR_FIELD_NAME_RU, true);
        else qb.orderBy(Investigator.INVESTIGATOR_FIELD_NAME_EN, true);
        List<Investigator> list = qb.query();
        List<Investigator> investigatorList = new ArrayList<>();
        for (Investigator investigator : list) {
            if (HelperFactory.getStaticHelper().getExpansionDAO().isEnableByID(investigator.expansionID)) investigatorList.add(investigator);
        }
        return investigatorList;
    }

    public List<Investigator> getInvestigatorsListByGameID(long gameID) throws SQLException {
        QueryBuilder<Investigator, Integer> qb = this.queryBuilder();
        qb.where().eq(Investigator.INVESTIGATOR_FIELD_GAME_ID, gameID);
        return qb.query();
    }

    public void deleteInvestigatorsByGameID(long id) throws SQLException {
        DeleteBuilder<Investigator, Integer> db = this.deleteBuilder();
        db.where().eq(Investigator.INVESTIGATOR_FIELD_GAME_ID, id);
        db.delete();
    }

    public GenericRawResults<String[]> getInvestigatorsCount(int ancientOneID) throws SQLException {
        String field;
        if (Localization.getInstance().isRusLocale()) field = Investigator.INVESTIGATOR_FIELD_NAME_RU;
        else field = Investigator.INVESTIGATOR_FIELD_NAME_EN;

        QueryBuilder<Game, Integer> qbGames = HelperFactory.getHelper().getGameDAO().queryBuilder();
        qbGames.selectColumns(Game.GAME_FIELD_ID);
        qbGames.where().eq(Game.GAME_FIELD_ANCIENT_ONE_ID, ancientOneID);

        QueryBuilder<Investigator, Integer> qb = this.queryBuilder();
        qb.selectRaw(field);
        qb.selectRaw("COUNT (" + field + ")");
        if (ancientOneID != 0) qb.where().in(Investigator.INVESTIGATOR_FIELD_GAME_ID, qbGames);
        qb.groupBy(field);
        qb.orderByRaw("COUNT (" + field + ") DESC");
        return qb.queryRaw();
    }
}