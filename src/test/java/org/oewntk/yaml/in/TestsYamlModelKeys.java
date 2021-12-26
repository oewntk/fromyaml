/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.yaml.in;

import org.junit.BeforeClass;
import org.junit.Test;
import org.oewntk.model.LibTestModelKeys;

import static org.junit.Assert.assertEquals;

public class TestsYamlModelKeys
{
	@BeforeClass
	public static void init()
	{
		TestsYamlCommon.init();
	}

	@Test
	public void testMobile()
	{
		int[] r = LibTestModelKeys.testMobile(TestsYamlCommon.model, TestsYamlCommon.ps);
		assertEquals(1, r[0]);
		assertEquals(1, r[1]);
		assertEquals(1, r[2]);
		assertEquals(2, r[3]);
		assertEquals(2, r[4]);
		assertEquals(5, r.length);
	}

	@Test
	public void testMobileNoPronunciation()
	{
		int[] r = LibTestModelKeys.testMobileNoPronunciation(TestsYamlCommon.model, TestsYamlCommon.ps);
		assertEquals(1, r[0]);
		assertEquals(1, r[1]);
		assertEquals(2, r[2]);
		assertEquals(2, r[3]);
		assertEquals(4, r.length);
	}

	@Test
	public void testEarthMulti()
	{
		int[] r = LibTestModelKeys.testEarthMulti(TestsYamlCommon.model, TestsYamlCommon.ps);
		assertEquals(1, r[0]);
		assertEquals(1, r[1]);
		assertEquals(2, r[2]);
		assertEquals(2, r[3]);
		assertEquals(4, r.length);
	}

	@Test
	public void testEarthMono()
	{
		int[] r = LibTestModelKeys.testEarthMono(TestsYamlCommon.model, TestsYamlCommon.ps);
		assertEquals(1, r[0]);
		assertEquals(1, r[1]);
		assertEquals(1, r[2]);
		assertEquals(1, r[3]);
		assertEquals(4, r.length);
	}

	@Test
	public void testBaroqueMulti()
	{
		int[] r = LibTestModelKeys.testBaroqueMulti(TestsYamlCommon.model, TestsYamlCommon.ps);
		assertEquals(1, r[0]);
		assertEquals(1, r[1]);
		assertEquals(2, r[2]);
		assertEquals(2, r[3]);
		assertEquals(1, r[4]);
		assertEquals(2, r[5]);
		assertEquals(3, r[6]);
		assertEquals(3, r[7]);
		assertEquals(0, r[8]);
		assertEquals(1, r[9]);
		assertEquals(1, r[10]);
		assertEquals(1, r[11]);
		assertEquals(0, r[12]);
		assertEquals(0, r[13]);
		assertEquals(0, r[14]);
		assertEquals(0, r[14]);
		assertEquals(16, r.length);
	}

	@Test
	public void testBaroqueMono()
	{
		int[] r = LibTestModelKeys.testBaroqueMono(TestsYamlCommon.model, TestsYamlCommon.ps);
		assertEquals(1, r[0]);
		assertEquals(1, r[1]);
		assertEquals(1, r[2]);
		assertEquals(1, r[3]);
		assertEquals(1, r[4]);
		assertEquals(1, r[5]);
		assertEquals(1, r[6]);
		assertEquals(1, r[7]);
		assertEquals(0, r[8]);
		assertEquals(1, r[9]);
		assertEquals(1, r[10]);
		assertEquals(1, r[11]);
		assertEquals(0, r[12]);
		assertEquals(0, r[13]);
		assertEquals(0, r[14]);
		assertEquals(0, r[14]);
		assertEquals(16, r.length);
	}

	@Test
	public void testCriticalMulti()
	{
		int[] r = LibTestModelKeys.testCriticalMulti(TestsYamlCommon.model, TestsYamlCommon.ps);
		assertEquals(1, r[0]);
		assertEquals(2, r[1]);
		assertEquals(1, r[2]);
		assertEquals(0, r[3]);
		assertEquals(4, r.length);
	}

	@Test
	public void testCriticalMono()
	{
		int[] r = LibTestModelKeys.testCriticalMono(TestsYamlCommon.model, TestsYamlCommon.ps);
		assertEquals(1, r[0]);
		assertEquals(1, r[1]);
		assertEquals(1, r[2]);
		assertEquals(0, r[3]);
		assertEquals(4, r.length);
	}

	@Test
	public void testBassDeep()
	{
		int[] r = LibTestModelKeys.testBassDeep(TestsYamlCommon.model, TestsYamlCommon.ps);
		assertEquals(1, r[0]);
		assertEquals(1, r[1]);
		assertEquals(2, r[2]);
		assertEquals(3, r.length);
	}

	@Test
	public void testBassShallow()
	{
		int[] r = LibTestModelKeys.testBassShallow(TestsYamlCommon.model, TestsYamlCommon.ps);
		assertEquals(1, r[0]);
		assertEquals(1, r[1]);
		assertEquals(2, r[2]);
		assertEquals(3, r.length);
	}

	@Test
	public void testRowDeep()
	{
		int[] r = LibTestModelKeys.testRowDeep(TestsYamlCommon.model, TestsYamlCommon.ps);
		assertEquals(1, r[0]);
		assertEquals(1, r[1]);
		assertEquals(2, r[2]);
		assertEquals(3, r.length);
	}

	@Test
	public void testRowShallow()
	{
		int[] r = LibTestModelKeys.testRowShallow(TestsYamlCommon.model, TestsYamlCommon.ps);
		assertEquals(1, r[0]);
		assertEquals(1, r[1]);
		assertEquals(2, r[2]);
		assertEquals(3, r.length);
	}
}
