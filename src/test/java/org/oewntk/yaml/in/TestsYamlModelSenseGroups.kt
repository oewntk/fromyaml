/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.yaml.in;

import org.junit.BeforeClass;
import org.junit.Test;
import org.oewntk.model.LibTestModelSenseGroups;

public class TestsYamlModelSenseGroups
{
	@BeforeClass
	public static void init()
	{
		TestsYamlCommon.init();
	}

	@Test
	public void testCISensesBaroque()
	{
		LibTestModelSenseGroups.testCISensesGroupingByLCLemma(TestsYamlCommon.model, "baroque", TestsYamlCommon.ps);
	}

	@Test
	public void testCISensesBaroqueA()
	{
		LibTestModelSenseGroups.testCISensesGroupingByLCLemmaAndPos(TestsYamlCommon.model, "baroque", 'a', TestsYamlCommon.ps);
	}

	@Test
	public void testCISensesCriticalA()
	{
		LibTestModelSenseGroups.testCISensesGroupingByLCLemmaAndPos(TestsYamlCommon.model, "critical", 'a', TestsYamlCommon.ps);
	}

	@Test
	public void testCISensesAiN()
	{
		LibTestModelSenseGroups.testCISensesGroupingByLCLemmaAndPos(TestsYamlCommon.model, "ai", 'n', TestsYamlCommon.ps);
	}

	@Test
	public void testCISensesAbsoluteA()
	{
		LibTestModelSenseGroups.testCISensesGroupingByLCLemmaAndPos(TestsYamlCommon.model, "absolute", 'a', TestsYamlCommon.ps);
	}
}
