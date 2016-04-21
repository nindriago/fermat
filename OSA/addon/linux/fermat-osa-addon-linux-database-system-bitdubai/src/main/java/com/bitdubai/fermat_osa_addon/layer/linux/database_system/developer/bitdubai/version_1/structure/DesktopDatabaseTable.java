/*
* @#DesktopDatabaseTable.java - 2015
* Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
* BITDUBAI/CONFIDENTIAL
*/
package com.bitdubai.fermat_osa_addon.layer.linux.database_system.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.all_definition.enums.interfaces.FermatEnum;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DataBaseAggregateFunctionType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DataBaseTableOrder;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseAggregateFunction;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterOperator;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterOrder;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFilter;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFilterGroup;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantDeleteRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantInsertRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantUpdateRecordException;
import com.bitdubai.fermat_osa_addon.layer.linux.database_system.developer.bitdubai.version_1.desktop.database.bridge.DesktopDatabaseBridge;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * The Class <code>com.bitdubai.fermat_osa_addon.layer.linux.database_system.developer.bitdubai.version_1.structure.DesktopDatabaseTable</code>
 * <p/>
 * Created by Hendry Rodriguez - (elnegroevaristo@gmail.com) on 10/12/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class DesktopDatabaseTable implements DatabaseTable {


    /**
     * DatabaseTable Member Variables.
     */
    String tableName;
    DesktopDatabaseBridge database;

    private List<DatabaseTableFilter> tableFilter;
    private List<DatabaseTableRecord> records;
    private DatabaseTableRecord tableRecord;
    private List<DataBaseTableOrder> tableOrder;
    private String top = "";
    private String offset = "";
    private DatabaseTableFilterGroup tableFilterGroup;
    private List<DatabaseAggregateFunction> tableSelectOperator;

    // Public constructor declarations.
    public DesktopDatabaseTable(DesktopDatabaseBridge database, String tableName) {
        this.tableName = tableName;
        //this.context = context;
        this.database = database;
    }

    @Override
    public List<DatabaseTableRecord> customQuery(String query, boolean customResult) throws CantLoadTableToMemoryException {
        return null;
    }

    /**
     * DatabaseTable interface implementation.
     */

    /**
     * <p>This method return a list of table columns names
     *
     * @return List<String> of columns names
     */

    public List<String> getColumns(ResultSet rs) {

        List<String> columns = new ArrayList<>();
        try {
            ResultSetMetaData rsmd = rs.getMetaData();

            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                columns.add(rsmd.getColumnName(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("error trying to get columns");
        }

        return columns;
    }


    /**
     * <p>This method return a list of Database Table Record objects
     *
     * @return List<DatabaseTableRecord> List of DatabaseTableRecord objects
     */
    @Override
    public List<DatabaseTableRecord> getRecords() {

        return this.records;
    }

    /**
     * <p>This method return a new empty instance of DatabaseTableRecord object
     *
     * @return DatabaseTableRecord object
     */
    @Override
    public DatabaseTableRecord getEmptyRecord() {

        return new DesktopDatabaseRecord();
    }

    /**
     * <p>This method clean Filter object
     */
    @Override
    public void clearAllFilters() {

        this.tableFilter = null;
    }

    @Override
    public DatabaseTableFilter getEmptyTableFilter() {
        return null;
    }

    @Override
    public DatabaseTableFilter getNewFilter(String column, DatabaseFilterType type, String value) {
        return null;
    }

    @Override
    public DatabaseTableFilterGroup getNewFilterGroup(List<DatabaseTableFilter> tableFilters, List<DatabaseTableFilterGroup> filterGroups, DatabaseFilterOperator filterOperator) {
        return null;
    }


    @Override
    public void updateRecord(DatabaseTableRecord record) throws CantUpdateRecordException {

        try {
            List<DatabaseRecord> records = record.getValues();

            //ContentValues recordUpdateList = new ContentValues();
            Map<String, Object> recordUpdateList = new HashMap<>();

            /**
             * I update only the fields marked as modified
             *
             */

            for (int i = 0; i < records.size(); ++i) {
                if (records.get(i).isChange())
                    recordUpdateList.put(records.get(i).getName(), records.get(i).getValue());
            }

            if (this.tableFilter != null) {
                this.database.update(tableName, recordUpdateList, makeFilter(), new String[]{tableFilter.get(0).getColumn()});
            } else {
                this.database.update(tableName, recordUpdateList, makeFilter(), null);
            }

        } catch (Exception exception) {
            throw new CantUpdateRecordException();
        }
    }

    @Override
    public void insertRecord(DatabaseTableRecord record) throws CantInsertRecordException {

        /**
         * First I get the table records with values.
         * and construct de ContentValues array for SqlLite
         */
        try {
            StringBuffer strRecords = new StringBuffer("");
            StringBuffer strValues = new StringBuffer("");

            List<DatabaseRecord> records = record.getValues();

            Map<String, Object> initialValues = new HashMap<String, Object>();
            //ContentValues initialValues = new ContentValues();

            for (int i = 0; i < records.size(); ++i) {
                initialValues.put(records.get(i).getName(), records.get(i).getValue());

                if (strRecords.length() > 0)
                    strRecords.append(",");
                strRecords.append(records.get(i).getName());

                if (strValues.length() > 0)
                    strValues.append(",");

                strValues.append("'" + records.get(i).getValue() + "'");

            }

            this.database.execSQL("INSERT INTO " + tableName + "(" + strRecords + ")" + " VALUES (" + strValues + ")");
        } catch (Exception exception) {
            throw new CantInsertRecordException(exception);
        }

    }

    @Override
    public long getCount() throws CantLoadTableToMemoryException {

        ResultSet rs = this.database.rawQuery("SELECT COUNT(*) as COUNT FROM " + tableName + makeFilter(), null);

        try {

            rs.next();

            return rs.getLong("COUNT");

        } catch (Exception e) {
            throw new CantLoadTableToMemoryException();
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void loadToMemory() throws CantLoadTableToMemoryException {

        this.records = new ArrayList<>();

        String topSentence = "";

        if (this.top.length() > 0)
            topSentence = " LIMIT " + this.top;

        ResultSet rs = this.database.rawQuery("SELECT  * FROM " + tableName + makeFilter() + makeOrder() + topSentence, null);
        try {

            if(rs.next()) {

                List<String> columns = getColumns(rs);

                do  {

                    DesktopDatabaseRecord tableRecordConsult = new DesktopDatabaseRecord();

                    for (String nameColumn : columns) {

                        tableRecordConsult.addValue(
                                new DesktopRecord(
                                        nameColumn,
                                        rs.getString(nameColumn),
                                        false
                                )
                        );
                    }

                    this.records.add(tableRecordConsult);

                } while (rs.next());
            }
        } catch (Exception e) {
            System.out.println("an error loading to memory");
            e.printStackTrace();
            throw new CantLoadTableToMemoryException(e);
        }

    }

    /**
     * <p>Check if the set will table in tableName variable exists
     *
     * @return boolean
     */
    @Override
    public boolean isTableExists() {

        ResultSet cursor = this.database.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + this.tableName + "'", null);
        try {
            ResultSetMetaData rsmd = cursor.getMetaData();

            if (rsmd.getColumnCount() > 0)
                return true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (cursor != null)
                    cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * <p>Sets the filter on a string field
     *
     * @param columName column name to filter
     * @param value     value to filter
     * @param type      DatabaseFilterType object
     */

    public void setStringFilter(String columName, String value, DatabaseFilterType type) {

        if (this.tableFilter == null)
            this.tableFilter = new ArrayList<DatabaseTableFilter>();

        DatabaseTableFilter filter = new DesktopDatabaseTableFilter();

        filter.setColumn(columName);
        filter.setValue(value);
        filter.setType(type);

        this.tableFilter.add(filter);
    }


    public void setFermatEnumFilter(String columnName, FermatEnum value, DatabaseFilterType type) {

    }

    @Override
    public void setFilterGroup(DatabaseTableFilterGroup filterGroup) {

        this.tableFilterGroup = filterGroup;

    }

    /**
     * <p>Sets the filter on a UUID field
     *
     * @param columName column name to filter
     * @param value     value to filter
     * @param type      DatabaseFilterType object
     */

    public void setUUIDFilter(String columName, UUID value, DatabaseFilterType type) {

        if (this.tableFilter == null)
            this.tableFilter = new ArrayList<DatabaseTableFilter>();

        DatabaseTableFilter filter = new DesktopDatabaseTableFilter();

        filter.setColumn(columName);
        filter.setValue(value.toString());
        filter.setType(type);

        this.tableFilter.add(filter);

    }

    /**
     * <p>Sets the order in which filtering field shown in ascendent or descending
     *
     * @param columnName Name of the column to sort
     * @param direction  DatabaseFilterOrder object
     */

    public void setFilterOrder(String columnName, DatabaseFilterOrder direction) {

        if (this.tableOrder == null)
            this.tableOrder = new ArrayList<DataBaseTableOrder>();

        DataBaseTableOrder order = new DesktopDatabaseTableOrder(columnName, direction);

        // order.setColumName(columnName);
        //order.setDirection(direction);


        this.tableOrder.add(order);
    }

    /**
     * <p>Sets the number of records to be selected in query
     *
     * @param top number of records to select (in string)
     */
    @Override
    public void setFilterTop(String top) {
        this.top = top;
    }

    @Override
    public void setFilterOffSet(String offset) {
        this.offset = offset;
    }

    @Override
    public void addStringFilter(String columnName, String value, DatabaseFilterType type) {
        if (this.tableFilter == null)
            this.tableFilter = new ArrayList<>();

        DatabaseTableFilter filter = new DesktopDatabaseTableFilter(
                columnName,
                type,
                value
        );

        this.tableFilter.add(filter);

    }

    @Override
    public void addFermatEnumFilter(String columnName, FermatEnum value, DatabaseFilterType type) {

        if (this.tableFilter == null)
            this.tableFilter = new ArrayList<>();

        this.tableFilter.add(
                new DesktopDatabaseTableFilter(
                        columnName,
                        type,
                        value.getCode()
                )
        );

    }

    @Override
    public void addFilterOrder(String columnName, DatabaseFilterOrder direction) {

        if (this.tableOrder == null)
            this.tableOrder = new ArrayList<>();

        DataBaseTableOrder order = new DesktopDatabaseTableOrder(columnName, direction);

        this.tableOrder.add(order);

    }

    @Override
    public void addUUIDFilter(String columName, UUID value, DatabaseFilterType type) {

        if (this.tableFilter == null)
            this.tableFilter = new ArrayList<>();

        DatabaseTableFilter filter = new DesktopDatabaseTableFilter();

        filter.setColumn(columName);
        filter.setValue(value.toString());
        filter.setType(type);

        this.tableFilter.add(filter);

    }

    @Override
    public void deleteRecord(DatabaseTableRecord record) throws CantDeleteRecordException {

    }

    @Override
    public DatabaseTableRecord getRecordFromPk(String pk) throws Exception {
        return null;
    }

    /**
     * <p>Sets the filter and subgroup to filter for queries with grouped where
     *
     * @param filters   list of DatabaseTableFilter object
     * @param subGroups list of DatabaseTableFilterGroup objects
     * @param operator  DatabaseFilterOperator enumerator
     */
    @Override
    public void setFilterGroup(List<DatabaseTableFilter> filters, List<DatabaseTableFilterGroup> subGroups, DatabaseFilterOperator operator) {

        DatabaseTableFilterGroup filterGroup = new DesktopDatabaseTableFilterGroup(filters, subGroups, operator);

       /* filterGroup.setFilters(filters);
        filterGroup.setSubGroups(subGroups);
        filterGroup.setOperator(operator);*/

        this.tableFilterGroup = filterGroup;
    }

    /**
     * DatabaseTable interface public void.
     */

    @Override
    public String makeFilter() {

        // I check the definition for the filter object, filter type, filter columns names
        // and build the WHERE statement
        String filter = "";
        StringBuffer strFilter = new StringBuffer();

        if (this.tableFilter != null) {
            for (int i = 0; i < tableFilter.size(); ++i) {

                strFilter.append(tableFilter.get(i).getColumn());

                switch (tableFilter.get(i).getType()) {
                    case EQUAL:
                        strFilter.append(" ='" + tableFilter.get(i).getValue() + "'");
                        break;
                    case GREATER_THAN:
                        strFilter.append(" > " + tableFilter.get(i).getValue());
                        break;
                    case LESS_THAN:
                        strFilter.append(" < " + tableFilter.get(i).getValue());
                        break;
                    case LIKE:
                        strFilter.append(" Like '%" + tableFilter.get(i).getValue() + "%'");
                        break;
                    default:
                        strFilter.append(" ");
                        break;
                }

                if (i < tableFilter.size() - 1)
                    strFilter.append(" AND ");

            }
        }

        filter = strFilter.toString();
        if (strFilter.length() > 0) filter = " WHERE " + filter;

        return filter;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public void addAggregateFunction(String columnName, DataBaseAggregateFunctionType operator, String alias) {

        if (this.tableSelectOperator == null)
            this.tableSelectOperator = new ArrayList<>();

        DatabaseAggregateFunction selectOperator = new DesktopDatabaseSelectOperator(
                columnName,
                operator,
                alias
        );

        this.tableSelectOperator.add(selectOperator);
    }

    @Override
    public List<DatabaseAggregateFunction> getTableAggregateFunction() {
        return tableSelectOperator;
    }

    private String makeOrder() {

        // I check the definition for the oder object, order direction, order columns names
        // and build the ORDER BY statement
        String order;
        StringBuilder strOrder = new StringBuilder();

        if (this.tableOrder != null) {
            for (int i = 0; i < tableOrder.size(); ++i) {

                switch (tableOrder.get(i).getDirection()) {
                    case DESCENDING:
                        strOrder.append(tableOrder.get(i).getColumnName())
                                .append(" DESC ");
                        break;
                    case ASCENDING:
                        strOrder.append(tableOrder.get(i).getColumnName());
                        break;
                    default:
                        strOrder.append(" ");
                        break;

                }
                if (i < tableOrder.size() - 1)
                    strOrder.append(" , ");
            }
        }

        order = strOrder.toString();
        if (strOrder.length() > 0) order = " ORDER BY " + order;

        return order;
    }


    private String makeInternalCondition(DatabaseTableFilter filter) {

        StringBuilder strFilter = new StringBuilder();

        strFilter.append(filter.getColumn());

        switch (filter.getType()) {
            case EQUAL:
                strFilter.append(" ='")
                        .append(filter.getValue())
                        .append("'");
                break;
            case NOT_EQUALS:
                strFilter.append(" <> '")
                        .append(filter.getValue())
                        .append("'");
                break;
            case GREATER_OR_EQUAL_THAN:
                strFilter.append(" >= ")
                        .append(filter.getValue());
                break;
            case GREATER_THAN:
                strFilter.append(" > ")
                        .append(filter.getValue());
                break;
            case LESS_OR_EQUAL_THAN:
                strFilter.append(" <= ")
                        .append(filter.getValue());
                break;
            case LESS_THAN:
                strFilter.append(" < ")
                        .append(filter.getValue());
                break;
            case LIKE:
                strFilter.append(" Like '%")
                        .append(filter.getValue())
                        .append("%'");
                break;
            case STARTS_WITH:
                strFilter.append(" Like '")
                        .append(filter.getValue())
                        .append("%'");
                break;
            case ENDS_WITH:
                strFilter.append(" Like '%")
                        .append(filter.getValue())
                        .append("'");
                break;
            default:
                strFilter.append(" ");
        }
        return strFilter.toString();
    }

    private String makeInternalConditionGroup(List<DatabaseTableFilter> filters, DatabaseFilterOperator operator) {

        StringBuffer strFilter = new StringBuffer();

        for (DatabaseTableFilter filter : filters) {
            switch (operator) {
                case AND:
                    if (strFilter.length() > 0)
                        strFilter.append(" AND ");

                    strFilter.append(makeInternalCondition(filter));
                    break;
                case OR:
                    if (strFilter.length() > 0)
                        strFilter.append(" OR ");

                    strFilter.append(makeInternalCondition(filter));
                    break;
                default:
                    strFilter.append(" ");
            }

        }
        return strFilter.toString();
    }

    public String makeGroupFilters(DatabaseTableFilterGroup databaseTableFilterGroup) {

        StringBuffer strFilter = new StringBuffer();
        String filter = "";

        if (databaseTableFilterGroup != null && (databaseTableFilterGroup.getFilters().size() > 0 || databaseTableFilterGroup.getSubGroups().size() > 0)) {
            strFilter.append("(");
            strFilter.append(makeInternalConditionGroup(databaseTableFilterGroup.getFilters(), databaseTableFilterGroup.getOperator()));

            int ix = 0;
            for (DatabaseTableFilterGroup subGroup : databaseTableFilterGroup.getSubGroups()) {
                if (subGroup.getFilters().size() > 0 || ix > 0) {
                    switch (databaseTableFilterGroup.getOperator()) {
                        case AND:
                            strFilter.append(" AND ");
                            break;
                        case OR:
                            strFilter.append(" OR ");
                            break;
                        default:
                            strFilter.append(" ");
                    }
                }
                strFilter.append("(");
                strFilter.append(makeGroupFilters(subGroup));
                strFilter.append(")");
                ix++;
            }
            strFilter.append(")");
        }

        filter = strFilter.toString();
        if (strFilter.length() > 0) filter = " WHERE " + filter;

        return filter;
    }


}
