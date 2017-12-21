package ru.mgusev.eldritchhorror.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import ru.mgusev.eldritchhorror.model.Prelude;

public class PreludeDAO extends BaseDaoImpl {

    public PreludeDAO(ConnectionSource connectionSource, Class dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<Prelude> getAllExpansion() throws SQLException{
        QueryBuilder<Prelude, Integer> qb = this.queryBuilder();
        return qb.query();
    }
}
