/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.yaml.in;

import org.junit.BeforeClass;
import org.junit.Test;
import org.oewntk.model.LibTestModelLexGroups;

public class TestsYamlModelLexGroups
{
	@BeforeClass
	public static void init()
	{
		TestsYamlCommon.init();
	}

	@Test
	public void testCIMultipleAll()
	{
		LibTestModelLexGroups.testCIMultipleAll(TestsYamlCommon.model, TestsYamlCommon.ps);
	}

	@Test
	public void testCILemmas()
	{
		LibTestModelLexGroups.testCILemmas(TestsYamlCommon.model, "battle of verdun", TestsYamlCommon.ps);
	}

	@Test
	public void testCICounts()
	{
		LibTestModelLexGroups.testCICounts(TestsYamlCommon.model, "battle of verdun", TestsYamlCommon.ps);
	}

	@Test
	public void testCICountsFromMap()
	{
		LibTestModelLexGroups.testCICountsFromMap(TestsYamlCommon.model, "battle of verdun", TestsYamlCommon.ps);
	}

	@Test
	public void testCIHypermapWest()
	{
		LibTestModelLexGroups.testCIHypermap3(TestsYamlCommon.model, "West", TestsYamlCommon.ps);
	}

	@Test
	public void testCIHypermapBaroque()
	{
		LibTestModelLexGroups.testCIHypermap3(TestsYamlCommon.model, "Baroque", TestsYamlCommon.ps);
	}

	@Test
	public void testCIAi()
	{
		LibTestModelLexGroups.testCILexesFor(TestsYamlCommon.model, "ai", TestsYamlCommon.ps);
	}

	@Test
	public void testCIBaroque()
	{
		LibTestModelLexGroups.testCILexesFor(TestsYamlCommon.model, "baroque", TestsYamlCommon.ps);
	}

	@Test
	public void testCIWest3()
	{
		LibTestModelLexGroups.testCILexesFor3(TestsYamlCommon.model, "West", TestsYamlCommon.ps);
	}

	@Test
	public void testCIBaroque3()
	{
		LibTestModelLexGroups.testCILexesFor3(TestsYamlCommon.model, "Baroque", TestsYamlCommon.ps);
	}

	@Test
	public void testCIAi3()
	{
		LibTestModelLexGroups.testCILexesFor3(TestsYamlCommon.model, "Ai", TestsYamlCommon.ps);
	}

	@Test
	public void testCIAbsolute3()
	{
		LibTestModelLexGroups.testCILexesFor3(TestsYamlCommon.model, "Absolute", TestsYamlCommon.ps);
	}
}
