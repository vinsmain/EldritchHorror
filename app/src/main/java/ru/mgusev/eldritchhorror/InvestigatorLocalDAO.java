package ru.mgusev.eldritchhorror;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

public class InvestigatorLocalDAO   extends BaseDaoImpl {
    protected InvestigatorLocalDAO(ConnectionSource connectionSource, Class dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<InvestigatorLocalDAO> getAllInvestigatorsLocal() throws SQLException{
        return this.queryForAll();
    }
}
