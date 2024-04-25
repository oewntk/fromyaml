/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.yaml.in;

import org.junit.BeforeClass;
import org.junit.Test;
import org.oewntk.model.LibTestModelQueries;

public class TestsYamlModelQueries
{
	@BeforeClass
	public static void init()
	{
		TestsYamlCommon.init();
	}

	@Test
	public void testRowByType()
	{
		LibTestModelQueries.testWordByType(TestsYamlCommon.model, "row", TestsYamlCommon.ps);
	}

	@Test
	public void testRowByPos()
	{
		LibTestModelQueries.testWordByType(TestsYamlCommon.model, "row", TestsYamlCommon.ps);
	}

	@Test
	public void testRowByTypeAndPronunciation()
	{
		LibTestModelQueries.testWordByTypeAndPronunciation(TestsYamlCommon.model, "row", TestsYamlCommon.ps);
	}

	@Test
	public void testRowByPosAndPronunciation()
	{
		LibTestModelQueries.testWordByTypeAndPronunciation(TestsYamlCommon.model, "row", TestsYamlCommon.ps);
	}

	@Test
	public void testCriticalByType()
	{
		LibTestModelQueries.testWordByType(TestsYamlCommon.model, "critical", TestsYamlCommon.ps);
	}

	@Test
	public void testCriticalByPos()
	{
		LibTestModelQueries.testWordByType(TestsYamlCommon.model, "critical", TestsYamlCommon.ps);
	}

	@Test
	public void testBassByPos()
	{
		LibTestModelQueries.testWordByType(TestsYamlCommon.model, "bass", TestsYamlCommon.ps);
	}

	@Test
	public void testBaroqueByPos()
	{
		LibTestModelQueries.testWordByType(TestsYamlCommon.model, "baroque", TestsYamlCommon.ps);
	}

	@Test
	public void testBaroqueCSByPos()
	{
		LibTestModelQueries.testWordByType(TestsYamlCommon.model, "Baroque", TestsYamlCommon.ps);
	}

	@Test
	public void testGaloreByPos()
	{
		LibTestModelQueries.testWordByType(TestsYamlCommon.model, "galore", TestsYamlCommon.ps);
	}
}
