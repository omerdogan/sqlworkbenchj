/*
 * PostgresSequenceReader.java
 *
 * This file is part of SQL Workbench/J, http://www.sql-workbench.net
 *
 * Copyright 2002-2013, Thomas Kellerer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at.
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * To contact the author please send an email to: support@sql-workbench.net
 *
 */
package workbench.db.postgres;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import workbench.db.SequenceDefinition;
import workbench.db.SequenceReader;
import workbench.db.TableIdentifier;
import workbench.db.WbConnection;
import workbench.log.LogMgr;
import workbench.storage.DataStore;
import workbench.util.SqlUtil;
import workbench.util.StringUtil;

/**
 * @author  Thomas Kellerer
 */
public class PostgresSequenceReader
	implements SequenceReader
{
	private WbConnection dbConnection;
	private final String baseSql = "SELECT min_value, max_value, increment_by, cache_value, is_cycled FROM ";

	public PostgresSequenceReader(WbConnection conn)
	{
		this.dbConnection = conn;
	}

	@Override
	public void readSequenceSource(SequenceDefinition def)
	{
		if (def == null) return;

		StringBuilder buf = new StringBuilder(250);

		try
		{
			String name = def.getSequenceName();
			Long max = (Long) def.getSequenceProperty("MAXVALUE");
			Long min = (Long) def.getSequenceProperty("MINVALUE");
			Long inc = (Long) def.getSequenceProperty("INCREMENT");
			Long cache = (Long) def.getSequenceProperty("CACHE");
			Boolean cycle = (Boolean) def.getSequenceProperty("CYCLE");
			if (cycle == null) cycle = Boolean.FALSE;

			buf.append("CREATE SEQUENCE ");
			buf.append(name);
			buf.append("\n       INCREMENT BY ");
			buf.append(inc);
			buf.append("\n       MINVALUE ");
			buf.append(min);
			long maxMarker = 9223372036854775807L;
			if (max != maxMarker)
			{
				buf.append("\n       MAXVALUE ");
				buf.append(max.toString());
			}
			buf.append("\n       CACHE ");
			buf.append(cache);
			buf.append("\n       ");
			if (!cycle.booleanValue())
			{
				buf.append("NO");
			}
			buf.append(" CYCLE");
			buf.append(";\n");

			if (StringUtil.isNonBlank(def.getComment()))
			{
				buf.append('\n');
				buf.append("COMMENT ON SEQUENCE ").append(def.getSequenceName()).append(" IS '").append(def.getComment().replace("'", "''")).append("';");
			}

			String col = def.getRelatedColumn();
			TableIdentifier tbl = def.getRelatedTable();
			if (tbl != null && StringUtil.isNonBlank(col))
			{
				buf.append('\n');
				buf.append("ALTER SEQUENCE ");
				buf.append(def.getSequenceName());
				buf.append(" OWNER TO ");
				buf.append(tbl.getTableName());
				buf.append('.');
				buf.append(col);
				buf.append(";\n");
			}
		}
		catch (Exception e)
		{
			LogMgr.logError("PgSequenceReader.getSequenceSource()", "Error reading sequence definition", e);
		}

		def.setSource(buf);
	}

	private void readRelatedTable(SequenceDefinition def)
	{
		if (def == null) return;
		String sql =
			"SELECT seq.relname as sequence_name,  \n" +
			"       n.nspname as sequence_schema, \n" +
			"       tab.relname as related_table,  \n" +
			"       col.attname as related_column \n" +
			"FROM pg_class seq  \n" +
			"  JOIN pg_depend d ON d.objid = seq.oid  \n" +
			"  JOIN pg_class tab ON d.objid = seq.oid AND d.refobjid = tab.oid  \n" +
			"  JOIN pg_attribute col ON (d.refobjid, d.refobjsubid) = (col.attrelid, col.attnum) \n" +
			"  JOIN pg_namespace n ON n.oid = seq.relnamespace  \n" +
			"WHERE seq.relkind = 'S'";

	  sql = "SELECT * FROM ( " + sql + ") t \n" +
			"WHERE sequence_name = ?" ;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Savepoint sp = null;

		try
		{
			sp = dbConnection.setSavepoint();
			if (def.getSchema() != null)
			{
				sql += " AND sequence_schema = ?";
			}
			pstmt = dbConnection.getSqlConnection().prepareStatement(sql);
			pstmt.setString(1, def.getObjectName());
			if (def.getSchema() != null)
			{
				pstmt.setString(2, def.getSchema());
			}
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				String tbl = rs.getString(3);
				String col = rs.getString(4);
				if (StringUtil.isNonBlank(tbl) && StringUtil.isNonBlank(col))
				{
					def.setRelatedTable(new TableIdentifier(def.getSchema(), tbl), col);
				}
			}
			dbConnection.releaseSavepoint(sp);
		}
		catch (SQLException e)
		{
			dbConnection.rollback(sp);
			LogMgr.logError("PostgresSequenceReader.getOwnedByClause()", "Error retrieving sequence column", e);
		}
		finally
		{
			SqlUtil.closeAll(rs, pstmt);
		}
	}

	/**
	 *	Return the source SQL for a PostgreSQL sequence definition.
	 *
	 *	@return The SQL to recreate the given sequence
	 */
	@Override
	public CharSequence getSequenceSource(String catalog, String schema, String aSequence)
	{
		SequenceDefinition def = getSequenceDefinition(catalog, schema, aSequence);
		return def.getSource();
	}

	/**
	 * Retrieve the list of full SequenceDefinitions from the database.
	 */
	@Override
	public List<SequenceDefinition> getSequences(String catalog, String schema, String namePattern)
	{
		List<SequenceDefinition> result = new ArrayList<SequenceDefinition>();

		ResultSet rs = null;
		Savepoint sp = null;
		if (namePattern == null) namePattern = "%";

		try
		{
			sp = this.dbConnection.setSavepoint();
			DatabaseMetaData meta = this.dbConnection.getSqlConnection().getMetaData();
			rs = meta.getTables(null, schema, namePattern, new String[] { "SEQUENCE"} );
			while (rs.next())
			{
				String seqName = rs.getString("TABLE_NAME");
				String seqSchema = rs.getString("TABLE_SCHEM");
				String remarks = rs.getString("REMARKS");
				result.add(retrieveSequenceDetails(seqSchema, seqName, remarks));
			}
			this.dbConnection.releaseSavepoint(sp);
		}
		catch (SQLException e)
		{
			this.dbConnection.rollback(sp);
			LogMgr.logError("PostgresSequenceReader.getSequences()", "Error retrieving sequences", e);
			return null;
		}
		finally
		{
			SqlUtil.closeResult(rs);
		}
		return result;
	}

	private SequenceDefinition createDefinition(String name, String schema, String comment, DataStore ds)
	{
		SequenceDefinition def = new SequenceDefinition(schema, name);
		def.setSequenceProperty("INCREMENT", ds.getValue(0, "increment_by"));
		def.setSequenceProperty("MAXVALUE", ds.getValue(0, "max_value"));
		def.setSequenceProperty("MINVALUE", ds.getValue(0, "min_value"));
		def.setSequenceProperty("CACHE", ds.getValue(0, "cache_value"));
		def.setSequenceProperty("CYCLE", ds.getValue(0, "is_cycled"));
		def.setSequenceProperty("REMARKS", comment);
		def.setComment(comment);
		return def;
	}

	private SequenceDefinition retrieveSequenceDetails(String schema, String sequence, String comment)
	{
		DataStore ds = getRawSequenceDefinition(null, schema, sequence);
		SequenceDefinition result = createDefinition(sequence, schema, comment, ds);
		readRelatedTable(result);
		readSequenceSource(result); // should be called after readRelatedTable() !!
		return result;
	}

	@Override
	public SequenceDefinition getSequenceDefinition(String catalog, String schema, String sequence)
	{
		ResultSet rs = null;
		Savepoint sp = null;

		boolean exists = false;
		String comment = null;

		try
		{
			sp = this.dbConnection.setSavepoint();
			DatabaseMetaData meta = this.dbConnection.getSqlConnection().getMetaData();
			rs = meta.getTables(null, schema, sequence, new String[] {"SEQUENCE"});
			if (rs.next())
			{
				comment = rs.getString("REMARKS");
				exists = true;
			}
			this.dbConnection.releaseSavepoint(sp);
		}
		catch (SQLException e)
		{
			this.dbConnection.rollback(sp);
			LogMgr.logError("PostgresSequenceReader.getSequenceDefinition()", "Error retrieving sequences", e);
			return null;
		}
		finally
		{
			SqlUtil.closeResult(rs);
		}

		if (exists)
		{
			return retrieveSequenceDetails(schema, sequence, comment);
		}
		return null;
	}

	@Override
	public DataStore getRawSequenceDefinition(String catalog, String schema, String sequence)
	{
		if (sequence == null) return null;

		String fullname = (schema == null ? sequence : schema + "." + sequence);

		DataStore result = null;
		Statement stmt = null;
		ResultSet rs = null;
		Savepoint sp = null;
		try
		{
			String sql = baseSql + fullname;
			sp = this.dbConnection.setSavepoint();
			stmt = this.dbConnection.createStatement();
			rs = stmt.executeQuery(sql);
			result = new DataStore(rs, true);
			this.dbConnection.releaseSavepoint(sp);
		}
		catch (SQLException e)
		{
			this.dbConnection.rollback(sp);
			LogMgr.logDebug("PgSequenceReader.getSequenceDefinition()", "Error reading sequence definition", e);
		}
		finally
		{
			SqlUtil.closeAll(rs, stmt);
		}
		return result;
	}

	@Override
	public String getSequenceTypeName()
	{
		return SequenceReader.DEFAULT_TYPE_NAME;
	}
}
