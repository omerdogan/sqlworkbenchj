/*
 * This file is part of SQL Workbench/J, http://www.sql-workbench.net
 *
 * Copyright 2002-2016 Thomas Kellerer.
 *
 * Licensed under a modified Apache License, Version 2.0 (the "License")
 * that restricts the use for certain governments.
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.sql-workbench.net/manual/license.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * To contact the author please send an email to: support@sql-workbench.net
 */
package workbench.db.importer;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import workbench.db.ColumnIdentifier;
import workbench.db.WbConnection;
import workbench.db.compare.BatchedStatement;
import workbench.db.postgres.PostgresArrayHandler;

/**
 *
 * @author Thomas Kellerer
 */
public interface ArrayValueHandler
{
  void setValue(BatchedStatement stmt, int columnIndex, Object data, ColumnIdentifier colInfo)
    throws SQLException;

  void setValue(PreparedStatement stmt, int columnIndex, Object data, ColumnIdentifier colInfo)
    throws SQLException;

  public static class Factory
  {
    public static ArrayValueHandler getInstance(WbConnection conn)
    {
      if (conn != null && conn.getMetadata().isPostgres())
      {
        return new PostgresArrayHandler(conn);
      }
      return null;
    }
  }
}