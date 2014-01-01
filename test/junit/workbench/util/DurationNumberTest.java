/*
 * This file is part of SQL Workbench/J, http://www.sql-workbench.net
 *
 * Copyright 2002-2014, Thomas Kellerer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * To contact the author please send an email to: support@sql-workbench.net
 */

package workbench.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Thomas Kellerer
 */
public class DurationNumberTest
{

	public DurationNumberTest()
	{
	}

	@Test
	public void testGetTime()
	{
		DurationNumber num = new DurationNumber();
		assertEquals(50, num.parseDefinition("50"));
		assertEquals(1000, num.parseDefinition(" 1s"));
		assertEquals(1000 * 60, num.parseDefinition(" 1 m "));
		assertEquals(1000 * 60 * 60 * 2, num.parseDefinition(" 2h"));
		assertEquals(1000 * 60 * 60 * 24, num.parseDefinition("1d"));
		assertEquals(1000 * 60 * 60 * 24 * 5, num.parseDefinition("5d"));
		assertEquals(0, num.parseDefinition("x"));
		assertEquals(0, num.parseDefinition(null));
	}

}
