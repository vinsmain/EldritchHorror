package ru.mgusev.eldritchhorror;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.List;

public class GameDAO extends BaseDaoImpl {
    protected GameDAO(ConnectionSource connectionSource, Class dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<Game> getAllGames() throws SQLException{
        return this.queryForAll();
    }

    public Game getTopGameToSort(boolean sort) throws SQLException {
        QueryBuilder<Game, Integer> qb = this.queryBuilder();
        qb.orderBy(Game.GAME_FIELD_SCORE, sort);
        return qb.queryForFirst();
    }

    public long writeGameToDB(Game game) throws SQLException {
        this.createOrUpdate(game);
        return this.queryRawValue("SELECT MAX(" + Game.GAME_FIELD_ID + ") from games");
    }
}
