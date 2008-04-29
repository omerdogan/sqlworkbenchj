/*
 * SyncDeleter.java
 *
 * This file is part of SQL Workbench/J, http://www.sql-workbench.net
 *
 * Copyright 2002-2008, Thomas Kellerer
 * No part of this code maybe reused without the permission of the author
 *
 * To contact the author please send an email to: support@sql-workbench.net
 *
 */
package workbench.db.compare;

import java.io.IOException;
import java.io.Writer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import workbench.db.ColumnIdentifier;
import workbench.db.TableIdentifier;
import workbench.db.WbConnection;
import workbench.log.LogMgr;
import workbench.resource.Settings;
import workbench.storage.ColumnData;
import workbench.storage.ResultInfo;
import workbench.storage.RowActionMonitor;
import workbench.storage.RowData;
import workbench.storage.SqlLiteralFormatter;
import workbench.util.SqlUtil;

/**
 * A class to delete rows in a target table that do not exist in a
 * reference table
 * 
 * The table that should be synchronized needs to exist in both the target and 
 * the reference database and it is expected that both tables have the same primary 
 * key definition.
 * 
 * The primary keys of the source table are not actually checked. The column names
 * of the PK of the target table are used to retrieve the data from the source
 * table.
 * 
 * @author support@sql-workbench.net
 */
public class TableSync
{
	private WbConnection toDelete;
	private WbConnection reference;
	private TableIdentifier checkTable;
	private TableIdentifier deleteTable;
	private BatchedStatement deleteStatement;
	private String deleteSql;
	private int chunkSize = 15;
	private int batchSize = 15;
	
	private Statement checkStatement;
	private Map<ColumnIdentifier, Integer> columnMap = new HashMap<ColumnIdentifier, Integer>();
	private RowActionMonitor monitor;
	private Writer outputWriter;
	private SqlLiteralFormatter formatter;
	private long deletedRows;
	
	public TableSync(WbConnection deleteFrom, WbConnection compareTo)
		throws SQLException
	{
		this.toDelete = deleteFrom;
		this.reference = compareTo;
		formatter = new SqlLiteralFormatter(toDelete);
		chunkSize = Settings.getInstance().getIntProperty("workbench.sql.deletesync.chunksize", 15);
	}

	public void setBatchSize(int size)
	{
		if (size > 0) this.batchSize = size;
	}
	
	public void setRowMonitor(RowActionMonitor rowMonitor)
	{
		this.monitor = rowMonitor;
	}
	
	/**
	 * Set a Writer to write the generated statements to. 
	 * If an outputwriter is defined, the statements will <b>not</b> 
	 * be executed against the target database.
	 * 
	 * @param out
	 */
	public void setOutputWriter(Writer out)
	{
		this.outputWriter = out;
	}
	
	public void setTableName(TableIdentifier checkTable)
		throws SQLException
	{
		setTableName(checkTable, checkTable.createCopy());
	}

	public long getDeletedRows()
	{
		return this.deletedRows;
	}
	
	/**
	 * Define the table to be checked. 
	 * 
	 * @param tableToCheck the table with the "reference" data
	 * @param tableToDelete the table from which obsolete rows should be deleted
	 * @throws java.sql.SQLException
	 */
	public void setTableName(TableIdentifier tableToCheck, TableIdentifier tableToDelete)
		throws SQLException
	{
		this.checkTable = this.reference.getMetadata().findSelectableObject(tableToCheck);
		this.deleteTable = this.toDelete.getMetadata().findTable(tableToDelete);
		
		this.columnMap.clear();
		
		List<ColumnIdentifier> columns = this.toDelete.getMetadata().getTableColumns(deleteTable);
		if (columns == null || columns.size() == 0) throw new SQLException("Table " + deleteTable.getTableName() + " not found in target database");
		String where = " WHERE ";
		int colIndex = 1;

		for (ColumnIdentifier col : columns)
		{
			if (col.isPkColumn())
			{
				if (colIndex > 1) 
				{
					where += " AND ";
				}
				String colname = this.toDelete.getMetadata().quoteObjectname(col.getColumnName());
				where += colname + " = ?";
				columnMap.put(col, colIndex);
				colIndex ++;
			}
		}
		
		if (columnMap.size() == 0)
		{
			throw new SQLException("No primary keys found in target table " + tableToDelete.getTableName());
		}
		this.deleteSql = "DELETE FROM " + this.deleteTable.getTableExpression(this.toDelete) + where;
		PreparedStatement deleteStmt = toDelete.getSqlConnection().prepareStatement(deleteSql);
		this.deleteStatement = new BatchedStatement(deleteStmt, toDelete, batchSize);
		LogMgr.logDebug("SyncDeleter.setTable()", "Using " + deleteSql + " to delete rows from target database");
	}

	public void deleteTarget()
		throws SQLException, IOException
	{

		List<ColumnIdentifier> columns = this.toDelete.getMetadata().getTableColumns(this.deleteTable);
		boolean first = true;
		String selectColumns = ""; 
		for (ColumnIdentifier col : columns)
		{
			if (col.isPkColumn())
			{
				if (!first)
				{
					selectColumns += ", ";
				}
				else first = false;
				selectColumns += " " + this.toDelete.getMetadata().quoteObjectname(col.getColumnName());
			}
		}
		String retrieve = "SELECT " + selectColumns + " FROM " + this.deleteTable.getTableExpression(this.toDelete);
		
		LogMgr.logDebug("SyncDeleter.deleteTarget()", "Using " + retrieve + " to retrieve rows from reference database");
	
		this.deletedRows = 0;
		checkStatement = reference.createStatement();
		
		ResultSet rs = null;
		Statement stmt = null;
		try
		{
			// Process all rows from the table to be synchronized
			stmt = this.toDelete.createStatementForQuery();
			rs = stmt.executeQuery(retrieve);
			ResultInfo info = new ResultInfo(rs.getMetaData(), this.reference);
			
			long rowNumber = 0;
			if (this.monitor != null)
			{
				this.monitor.setMonitorType(RowActionMonitor.MONITOR_DELETE);
				this.monitor.setCurrentObject(this.deleteTable.getTableExpression(this.toDelete), -1, -1);
			}
			int cols = info.getColumnCount();
			List<RowData> packetRows = new ArrayList<RowData>(chunkSize);
			
			while (rs.next())
			{
				rowNumber ++;
				RowData row = new RowData(cols);
				if (this.monitor != null)
				{
					monitor.setCurrentRow(rowNumber, -1);
				}
				row.read(rs, info);
				packetRows.add(row);
				
				if (packetRows.size() == chunkSize)
				{
					checkRows(packetRows, info);
					packetRows.clear();
				}
			}
			
			if (packetRows.size() > 0)
			{
				checkRows(packetRows, info);
			}
			
			this.deletedRows += this.deleteStatement.flush();
			
			if (!toDelete.getAutoCommit())
			{
				toDelete.commit();
			}
			if (this.monitor != null)
			{
				this.monitor.jobFinished();
			}
			
		}
		catch (SQLException e)
		{
			if (!toDelete.getAutoCommit())
			{
				try { toDelete.rollback(); } catch (Throwable th) {}
			}
			throw e;
		}
		finally
		{
			SqlUtil.closeResult(rs);
			this.deleteStatement.close();
			SqlUtil.closeStatement(this.checkStatement);
		}
	}

	private void checkRows(List<RowData> referenceRows, ResultInfo info)
		throws SQLException, IOException
	{
		String sql = buildCheckSql(referenceRows, info);
		ResultSet rs = null;
		try
		{
			rs = checkStatement.executeQuery(sql);
			List<RowData> checkRows = new ArrayList<RowData>(referenceRows.size());
			ResultInfo ri = new ResultInfo(rs.getMetaData(), reference);
			while (rs.next())
			{
				RowData r = new RowData(ri);
				r.read(rs, ri);
				checkRows.add(r);
			}
			
			// Same number of rows --> no row is missing 
			if (checkRows.size() == referenceRows.size()) return;
			for (RowData doomed : referenceRows)
			{
				if (!checkRows.contains(doomed))
				{
					deleteRow(doomed, ri);
				}
			}
		}
		catch (SQLException e)
		{
			LogMgr.logError("TableSync.checkRows()", "Error when running check SQL " + sql, e);
			throw e;
		}
		finally
		{
			SqlUtil.closeResult(rs);
		}
		return;
	}

	private void deleteRow(RowData row, ResultInfo info)
		throws SQLException
	{
		if (this.outputWriter == null)
		{
			for (int i=0; i < row.getColumnCount(); i++)
			{
				Integer index = this.columnMap.get(info.getColumn(i));
				this.deleteStatement.setObject(index, row.getValue(i));
			}
			long rows = deleteStatement.executeUpdate();
			this.deletedRows += rows;
		}
		else
		{
			try
			{
				this.outputWriter.write("DELETE FROM " + deleteTable.getTableName() + " WHERE ") ;
				for (int i=0; i < row.getColumnCount(); i++)
				{
					Object value = row.getValue(i);
					ColumnIdentifier col = info.getColumn(i);
					if (i > 0) this.outputWriter.write(" AND ");
					outputWriter.write(col.getColumnName());
					outputWriter.write(" = ");
					outputWriter.write(formatter.getDefaultLiteral(new ColumnData(value, col)).toString());
				}
				outputWriter.write(";\n");
			}
			catch (IOException e)
			{
				LogMgr.logError("TableSync.deleteRow()", "Error writing DELETE statement", e);
			}
		}
		return; 
	}
	
	private String buildCheckSql(List<RowData> rows, ResultInfo info)
	{
		StringBuffer sql = new StringBuffer(150);
		sql.append("SELECT ");
		for (int i=0; i < info.getColumnCount(); i++)
		{
			if (i > 0) sql.append(',');
			sql.append(info.getColumnName(i));
		}
		sql.append(" FROM ");
		sql.append(this.checkTable.getTableExpression(reference));
		sql.append(" WHERE ");
		
		for (int row=0; row < rows.size(); row++)
		{
			if (row > 0) sql.append (" OR ");
			sql.append("(");
			for (int c=0; c < info.getColumnCount(); c++)
			{
				if (c > 0) sql.append(" AND ");
				sql.append(info.getColumnName(c));
				sql.append(" = ");
				Object value = rows.get(row).getValue(c);
				ColumnIdentifier col = info.getColumn(c);
				ColumnData data = new ColumnData(value,col);
				sql.append(formatter.getDefaultLiteral(data));
			}
			sql.append(") ");
		}
		return sql.toString();
	}
}
