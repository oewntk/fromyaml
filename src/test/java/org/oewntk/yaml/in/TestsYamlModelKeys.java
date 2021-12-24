/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.yaml.in;

import org.junit.BeforeClass;
import org.junit.Test;
import org.oewntk.model.LibTestModelKeys;

public class TestsYamlModelKeys
{
	@BeforeClass
	public static void init()
	{
		TestsYamlCommon.init();
	}

	@Test
	public void testEarthMulti()
	{
		LibTestModelKeys.testEarthMulti(TestsYamlCommon.model, TestsYamlCommon.ps);
	}

	@Test
	public void testEarthMono()
	{
		LibTestModelKeys.testEarthMono(TestsYamlCommon.model, TestsYamlCommon.ps);
	}

	@Test
	public void testBaroqueMulti()
	{
		LibTestModelKeys.testBaroqueMulti(TestsYamlCommon.model, TestsYamlCommon.ps);
	}

	@Test
	public void testBaroqueMono()
	{
		LibTestModelKeys.testBaroqueMono(TestsYamlCommon.model, TestsYamlCommon.ps);
	}

	@Test
	public void testMobile()
	{
		LibTestModelKeys.testMobile(TestsYamlCommon.model, TestsYamlCommon.ps);
	}

	@Test
	public void testBassDeep()
	{
		LibTestModelKeys.testBassDeep(TestsYamlCommon.model, TestsYamlCommon.ps);
	}

	@Test
	public void testBassShallow()
	{
		LibTestModelKeys.testBassShallow(TestsYamlCommon.model, TestsYamlCommon.ps);
	}

	@Test
	public void testRowDeep()
	{
		LibTestModelKeys.testRowDeep(TestsYamlCommon.model, TestsYamlCommon.ps);
	}

	@Test
	public void testRowShallow()
	{
		LibTestModelKeys.testRowShallow(TestsYamlCommon.model, TestsYamlCommon.ps);
	}

	@Test
	public void testCriticalDeep()
	{
		LibTestModelKeys.testCriticalDeep(TestsYamlCommon.model, TestsYamlCommon.ps);
	}

	@Test
	public void testCriticalPos()
	{
		LibTestModelKeys.testCriticalPos(TestsYamlCommon.model, TestsYamlCommon.ps);
	}

	@Test
	public void testCriticalPWN()
	{
		LibTestModelKeys.testCriticalPWN(TestsYamlCommon.model, TestsYamlCommon.ps);
	}
}
