/*
 * TableCreator.java
 *
 * This file is part of SQL Workbench/J, http://www.sql-workbench.net
 *
 * Copyright 2002-2010, Thomas Kellerer
 * No part of this code maybe reused without the permission of the author
 *
 * To contact the author please send an email to: support@sql-workbench.net
 *
 */
package workbench.db;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import workbench.log.LogMgr;
import workbench.util.SqlUtil;
import workbench.util.StringUtil;

/**
 * A class to create a table in the database based on column definitions.
 * 
 * @author  Thomas Kellerer
 */
public class TableCreator
{
	private WbConnection connection;
	private List<ColumnIdentifier> columnDefinition;
	private TableIdentifier tablename;
	private TypeMapper mapper;
	private boolean useDbmsDataType;
	private boolean useColumnAlias;
	private String creationType;

	/**
	 * Create a new TableCreator.
	 *
	 * @param target the connection where to create the table
	 * @param type a keyword identifying the type of the table. This will be used to retrieve the corresponding
	 *             SQL template from {@link DbSettings#getCreateTableTemplate(workbench.db.TableCreator.CreationType)
	 * @param newTable the name of the new table
	 * @param columns the columns of the new table
	 * @throws SQLException if something goes wrong
	 */
	public TableCreator(WbConnection target, String type, TableIdentifier newTable, Collection<ColumnIdentifier> columns)
		throws SQLException
	{
		this.connection = target;
		this.tablename = newTable.createCopy();
		
		// As we are sorting the columns we have to create a copy of the array
		// to ensure that the caller does not see a different ordering
		this.columnDefinition = new ArrayList<ColumnIdentifier>(columns);
		
		// Now sort the columns according to their DBMS position
		ColumnIdentifier.sortByPosition(columnDefinition);

		this.mapper = new TypeMapper(this.connection);
		creationType = type;
	}

	public void setUseColumnAlias(boolean flag)
	{
		this.useColumnAlias = flag;
	}
	
	public void useDbmsDataType(boolean flag)
	{
		this.useDbmsDataType = flag;
	}

	public TableIdentifier getTable()
	{
		return this.tablename;
	}
	
	public void createTable()
		throws SQLException
	{
		StringBuilder columns = new StringBuilder(100);
		String template = connection.getDbSettings().getCreateTableTemplate(creationType);
		String name = this.tablename.getTableExpression(this.connection);
		
		int numCols = 0;
		List<String> pkCols = new ArrayList<String>();
		
		for (ColumnIdentifier col : columnDefinition)
		{
			if (col.isPkColumn()) pkCols.add(col.getColumnName());
			String def = this.getColumnDefintionString(col);
			if (def == null) continue;
			
			if (numCols > 0) columns.append(", ");
			columns.append(def);
			numCols++;
		}

		String sql = template.replace(MetaDataSqlManager.FQ_TABLE_NAME_PLACEHOLDER, name);
		sql = sql.replace(MetaDataSqlManager.COLUMN_LIST_PLACEHOLDER, columns);

		LogMgr.logInfo("TableCreator.createTable()", "Creating table using sql: " + sql);
		Statement stmt = this.connection.createStatement();
		try
		{
			stmt.executeUpdate(sql);
			
			if (pkCols.size() > 0)
			{
				TableSourceBuilder builder = TableSourceBuilderFactory.getBuilder(connection);
				CharSequence pkSql = builder.getPkSource(this.tablename, pkCols, null);
				if (pkSql.length() > 0)
				{
					LogMgr.logInfo("TableCreator.createTable()", "Adding primary key using: " + pkSql.toString());
					stmt.executeUpdate(pkSql.toString());
				}
			}
			
			if (this.connection.getDbSettings().ddlNeedsCommit() && !this.connection.getAutoCommit())
			{
				LogMgr.logDebug("TableCreator.createTable()", "Commiting the changes");
				this.connection.commit();
			}
		}
		finally
		{
			SqlUtil.closeStatement(stmt);
		}
	}

	/**
	 * Return the SQL string for the column definition.
	 * If useDbmsDataType is set to true, then the data type
	 * stored in the ColumnIdentifier is used. Otherwise
	 * the TypeMapper is use to map the jdbc data type returned from
	 * ColumnIdentifier.getDataType() to the target DBMS
	 *
	 * @see ColumnIdentifier#getDataType() 
	 * @see TypeMapper#getTypeName(int, int, int)
	 */
	private String getColumnDefintionString(ColumnIdentifier col)
	{
		if (col == null) return null;

		int type = col.getDataType();
		int size = col.getColumnSize();
		int digits = col.getDecimalDigits();
		String name = (useColumnAlias ? col.getDisplayName() : col.getColumnName());

		StringBuilder result = new StringBuilder(30);
		boolean isKeyword = connection.getMetadata().isKeyword(name);
		name = SqlUtil.quoteObjectname(name, isKeyword);
		result.append(name);
		result.append(' ');

		String typeName = null;
		if (this.useDbmsDataType)
		{
			typeName = col.getDbmsType();
		}
		else
		{
			typeName = this.mapper.getTypeName(type, size, digits);
		}
		result.append(typeName);

		if (!col.isNullable())
		{
			result.append(" NOT NULL");
		}

		return result.toString();
	}

}
