package ru.mgusev.eldritchhorror.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.List;

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

    public Game getTopGameToSort(boolean sort) throws SQLException {
        QueryBuilder<Game, Integer> qb = this.queryBuilder();
        qb.where().eq(Game.GAME_FIELD_WIN_GAME, true);
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
        queryRaw("DELETE FROM " + Game.GAME_TABLE_NAME);
    }
}
