package ru.mgusev.eldritchhorror;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

public class InvestigatorDAO extends BaseDaoImpl {
    protected InvestigatorDAO(ConnectionSource connectionSource, Class dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<Investigator> getAllInvestigators() throws SQLException{
        return this.queryForAll();
    }
}
