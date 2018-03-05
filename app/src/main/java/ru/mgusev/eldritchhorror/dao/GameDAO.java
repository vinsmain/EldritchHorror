package ru.mgusev.eldritchhorror.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.mgusev.eldritchhorror.database.HelperFactory;
import ru.mgusev.eldritchhorror.model.Game;

public class GameDAO extends BaseDaoImpl {
    public GameDAO(ConnectionSource connectionSource, Class dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<Game> getGamesSortDateUp() throws SQLException{
        QueryBuilder<Game, Integer> qb = this.queryBuilder();
        qb.orderBy(Game.GAME_FIELD_DATE, true);
        qb.orderBy(Game.GAME_FIELD_ID, true);
        return qb.query();
    }

    public List<Game> getGamesSortDateDown() throws SQLException{
        QueryBuilder<Game, Integer> qb = this.queryBuilder();
        qb.orderBy(Game.GAME_FIELD_DATE, false);
        qb.orderBy(Game.GAME_FIELD_ID, false);
        return qb.query();
    }

    public List<Game> getGamesSortAncientOne() throws SQLException{
        QueryBuilder<Game, Integer> qb = this.queryBuilder();
        qb.orderBy(Game.GAME_FIELD_ANCIENT_ONE_ID, true);
        qb.orderBy(Game.GAME_FIELD_WIN_GAME, false);
        qb.orderBy(Game.GAME_FIELD_SCORE, true);
        qb.orderBy(Game.GAME_FIELD_DATE, false);
        qb.orderBy(Game.GAME_FIELD_ID, false);
        return qb.query();
    }

    public List<Game> getGamesSortScoreUp() throws SQLException{
        QueryBuilder<Game, Integer> qb = this.queryBuilder();
        qb.orderBy(Game.GAME_FIELD_WIN_GAME, true);
        qb.orderBy(Game.GAME_FIELD_SCORE, false);
        qb.orderBy(Game.GAME_FIELD_DATE, true);
        qb.orderBy(Game.GAME_FIELD_ID, true);
        return qb.query();
    }

    public List<Game> getGamesSortScoreDown() throws SQLException{
        QueryBuilder<Game, Integer> qb = this.queryBuilder();
        qb.orderBy(Game.GAME_FIELD_WIN_GAME, false);
        qb.orderBy(Game.GAME_FIELD_SCORE, true);
        qb.orderBy(Game.GAME_FIELD_DATE, false);
        qb.orderBy(Game.GAME_FIELD_ID, false);
        return qb.query();
    }

    public Game getTopGameToSort(boolean sort, int ancienOneID) throws SQLException {
        QueryBuilder<Game, Integer> qb = this.queryBuilder();
        qb.where().eq(Game.GAME_FIELD_WIN_GAME, true);
        if (ancienOneID != 0) qb.where().eq(Game.GAME_FIELD_ANCIENT_ONE_ID, ancienOneID);
        qb.orderBy(Game.GAME_FIELD_SCORE, sort);
        return qb.queryForFirst();
    }

    public void writeGameToDB(Game game) throws SQLException {
        this.createOrUpdate(game);
    }

    public boolean hasGame(Game game) throws SQLException {
        QueryBuilder<Game, Integer> qb = this.queryBuilder();
        qb.where().eq(Game.GAME_FIELD_ID, game.id);
        return qb.query().size() != 0;
    }

    public void clearTable() throws SQLException {
        for (Game game : getGamesSortScoreUp()) {
            if (game.userID != null) {
                HelperFactory.getHelper().getInvestigatorDAO().deleteInvestigatorsByGameID(game.id);
                delete(game);
            }
        }
    }

    public Game getGameByID(Game game) throws SQLException {
        QueryBuilder<Game, Integer> qb = this.queryBuilder();
        qb.where().eq(Game.GAME_FIELD_ID, game.id);
        return qb.queryForFirst();
    }

    public List<Game> getGamesByAncientOne(String ancientOneName) throws SQLException{
        int ancientOneID = HelperFactory.getStaticHelper().getAncientOneDAO().getAncientOneIDByName(ancientOneName);
        QueryBuilder<Game, Integer> qb = this.queryBuilder();
        qb.where().eq(Game.GAME_FIELD_ANCIENT_ONE_ID, ancientOneID);
        return qb.query();
    }

    public GenericRawResults<String[]> getAncientOneCount() throws SQLException {
        QueryBuilder<Game, Integer> qb = this.queryBuilder();
        qb.selectRaw(Game.GAME_FIELD_ANCIENT_ONE_ID);
        qb.selectRaw("COUNT (" + Game.GAME_FIELD_ADVENTURE_ID + ")");
        qb.groupBy(Game.GAME_FIELD_ANCIENT_ONE_ID);
        qb.orderByRaw("COUNT (" + Game.GAME_FIELD_ADVENTURE_ID + ") DESC");
        return qb.queryRaw();
    }

    public GenericRawResults<String[]> getScoreCount(int ancientOneID) throws SQLException {
        QueryBuilder<Game, Integer> qb = this.queryBuilder();
        qb.selectRaw(Game.GAME_FIELD_SCORE);
        qb.selectRaw("COUNT (" + Game.GAME_FIELD_SCORE + ")");
        if (ancientOneID == 0) qb.where().eq(Game.GAME_FIELD_WIN_GAME, true);
        else qb.where().eq(Game.GAME_FIELD_WIN_GAME, true).and().eq(Game.GAME_FIELD_ANCIENT_ONE_ID, ancientOneID);
        qb.groupBy(Game.GAME_FIELD_SCORE);
        //qb.orderByRaw("COUNT (" + Game.GAME_FIELD_SCORE + ") DESC");
        qb.orderByRaw(Game.GAME_FIELD_SCORE);
        return qb.queryRaw();
    }

    public List<Float> getDefeatReasonCount(int ancientOneID) throws SQLException {
        List<Float> results = new ArrayList<>();
        QueryBuilder<Game, Integer> qb = this.queryBuilder();
        List<String> fields = Arrays.asList(Game.GAME_FIELD_DEFEAT_BY_AWAKENED_ANCIENT_ONE, Game.GAME_FIELD_DEFEAT_BY_ELIMINATION, Game.GAME_FIELD_DEFEAT_BY_MYTHOS_DEPLETION);
        for (String field : fields) {
            Where where = qb.where();
            where.eq(Game.GAME_FIELD_WIN_GAME, false);
            where.and();
            where.eq(field, true);
            if (ancientOneID != 0) {
                where.and();
                where.eq(Game.GAME_FIELD_ANCIENT_ONE_ID, ancientOneID);
            }
            results.add((float) qb.query().size());
        }
        return results;
    }

    public Game getLastGame(int ancientOneID) throws SQLException{
        QueryBuilder<Game, Integer> qb = this.queryBuilder();
        if (ancientOneID != 0) qb.where().eq(Game.GAME_FIELD_ANCIENT_ONE_ID, ancientOneID);
        qb.orderBy(Game.GAME_FIELD_DATE, true);
        qb.orderBy(Game.GAME_FIELD_ID, true);
        return qb.queryForFirst();
    }
}