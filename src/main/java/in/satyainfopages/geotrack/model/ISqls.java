package in.satyainfopages.geotrack.model;

/**
 * Created by DalbirSingh on 15-12-2014.
 */
public interface ISqls {
    String getDropSql();

    String getTableName();

    String[] getAllColumns();

    String getSelectSql();

    String getCreateSql();

    String getInsertSql();

    String getDeleteSql();
}
