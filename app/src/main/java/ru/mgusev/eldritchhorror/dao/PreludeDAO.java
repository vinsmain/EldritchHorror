package ru.mgusev.eldritchhorror.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.mgusev.eldritchhorror.database.HelperFactory;
import ru.mgusev.eldritchhorror.model.Localization;
import ru.mgusev.eldritchhorror.model.Prelude;

public class PreludeDAO extends BaseDaoImpl {

    public PreludeDAO(ConnectionSource connectionSource, Class dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<Prelude> getAllExpansion() throws SQLException{
        QueryBuilder<Prelude, Integer> qb = this.queryBuilder();
        return qb.query();
    }

    public List<String> getPreludeNameList() throws SQLException {
        QueryBuilder<Prelude, Integer> qb = this.queryBuilder();
        if (Locale.getDefault().getLanguage().equals("ru")) qb.orderBy(Prelude.PRELUDE_FIELD_NAME_RU, true);
        else qb.orderBy(Prelude.PRELUDE_FIELD_NAME_EN, true);
        List<Prelude> preludeList = qb.query();
        List<String> nameList = new ArrayList<>();
        for (Prelude prelude : preludeList) {
            if (HelperFactory.getStaticHelper().getExpansionDAO().isEnableByID(prelude.expansionID)) nameList.add(prelude.getName());
        }
        return nameList;
    }

    public String getPreludeNameByID(int id) throws SQLException {
        QueryBuilder<Prelude, Integer> qb = this.queryBuilder();
        qb.where().eq(Prelude.PRELUDE_FIELD_ID, id);
        return qb.queryForFirst().getName();
    }

    public int getPreludeIDByName(String name) throws SQLException {
        if (name.contains("'")) name = name.replace("'", "''");
        QueryBuilder<Prelude, Integer> qb = this.queryBuilder();
        if (Localization.getInstance().isRusLocale()) qb.where().eq(Prelude.PRELUDE_FIELD_NAME_RU, name);
        else qb.where().eq(Prelude.PRELUDE_FIELD_NAME_EN, name);
        return qb.queryForFirst().id;
    }

    public Prelude getPreludeByName(String name) throws SQLException {
        if (name.contains("'")) name = name.replace("'", "''");
        QueryBuilder<Prelude, Integer> qb = this.queryBuilder();
        if (Localization.getInstance().isRusLocale()) qb.where().eq(Prelude.PRELUDE_FIELD_NAME_RU, name);
        else qb.where().eq(Prelude.PRELUDE_FIELD_NAME_EN, name);
        return qb.queryForFirst();
    }
}
