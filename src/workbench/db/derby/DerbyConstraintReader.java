/*
 * DerbyConstraintReader.java
 *
 * This file is part of SQL Workbench/J, http://www.sql-workbench.net
 *
 * Copyright 2002-2008, Thomas Kellerer
 * No part of this code maybe reused without the permission of the author
 *
 * To contact the author please send an email to: support@sql-workbench.net
 *
 */
package workbench.db.derby;

import workbench.db.AbstractConstraintReader;

/**
 * Constraint reader for the Derby database
 * @author  support@sql-workbench.net
 */
public class DerbyConstraintReader 
	extends AbstractConstraintReader
{
	private static final String TABLE_SQL = "select 'check '|| c.checkdefinition \n" + 
             "from sys.syschecks c, sys.systables t, sys.sysconstraints cons, sys.sysschemas s \n" + 
             "where t.tableid = cons.tableid \n" + 
             "and   t.schemaid = s.schemaid \n" + 
             "and   cons.constraintid = c.constraintid \n" + 
             "and   t.tablename = ? \n" + 
             "and   s.schemaname = ?";

						 
	public DerbyConstraintReader()
	{
	}
	
	public String getColumnConstraintSql() { return null; }
	public String getTableConstraintSql() { return TABLE_SQL; }
	
	public int getIndexForTableNameParameter() { return 1; }
	public int getIndexForSchemaParameter() { return 2; }
}
