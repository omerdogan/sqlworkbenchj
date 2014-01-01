/*
 * PkMappingTest.java
 *
 * This file is part of SQL Workbench/J, http://www.sql-workbench.net
 *
 * Copyright 2002-2014, Thomas Kellerer
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
package workbench.storage;

import java.util.List;
import workbench.TestUtil;
import workbench.db.TableIdentifier;
import workbench.util.WbFile;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Thomas Kellerer
 */
public class PkMappingTest
{

	@Test
	public void testMapping()
	{
		TestUtil util = new TestUtil("PkMappingTest");
		WbFile f = new WbFile(util.getBaseDir(), "mapping_test.properties");
		PkMapping map = new PkMapping(f.getFullPath());
		TableIdentifier tbl = new TableIdentifier("PERSON");
		map.addMapping(tbl, "id");

		TableIdentifier tbl2 = new TableIdentifier("person");
		List<String> col = map.getPKColumns(tbl2);
		assertEquals(1, col.size());
		assertEquals("id", col.get(0));
	}
}
