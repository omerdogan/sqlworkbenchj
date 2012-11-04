/*
 * HtmlUtilTest.java
 *
 * This file is part of SQL Workbench/J, http://www.sql-workbench.net
 *
 * Copyright 2002-2012, Thomas Kellerer
 * No part of this code maybe reused without the permission of the author
 *
 * To contact the author please send an email to: support@sql-workbench.net
 *
 */
package workbench.util;

import java.awt.Color;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Thomas Kellerer
 */
public class HtmlUtilTest
{

	@Test
	public void testHtmlColor()
	{
		assertEquals("ff0000", HtmlUtil.getHtmlColor(Color.RED));
		assertEquals("00ff00", HtmlUtil.getHtmlColor(Color.GREEN));
		assertEquals("0000ff", HtmlUtil.getHtmlColor(Color.BLUE));
		assertEquals("000000", HtmlUtil.getHtmlColor(Color.BLACK));
		assertEquals("ffffff", HtmlUtil.getHtmlColor(Color.WHITE));
	}

	@Test
  public void testEscapeHTML()
	{
		String input = "<sometag> sometext";
		String escaped = HtmlUtil.escapeHTML(input);
		assertEquals("&lt;sometag&gt; sometext", escaped);

		input = "a &lt; b";
		escaped = HtmlUtil.escapeHTML(input);
		assertEquals("a &amp;lt; b", escaped);
	}

	@Test
  public void testEscapeXML()
	{
		String input = "<sometag> sometext";
		String escaped = HtmlUtil.escapeXML(input);
		assertEquals("&lt;sometag&gt; sometext", escaped);

		input = "a &lt; b";
		escaped = HtmlUtil.escapeXML(input);
		assertEquals("a &amp;lt; b", escaped);

		input = "a > b";
		escaped = HtmlUtil.escapeXML(input);
		assertEquals("a &gt; b", escaped);

		input = "a '>' b";
		escaped = HtmlUtil.escapeXML(input, false);
		assertEquals("a '&gt;' b", escaped);

		input = "& > < \"";
		escaped = HtmlUtil.escapeXML(input, false);
		assertEquals("&amp; &gt; &lt; &quot;", escaped);
	}
}
