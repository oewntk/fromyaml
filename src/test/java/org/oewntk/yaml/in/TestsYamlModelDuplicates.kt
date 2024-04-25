/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.yaml.in;

import org.junit.BeforeClass;
import org.junit.Test;
import org.oewntk.model.LibTestModelDuplicates;

public class TestsYamlModelDuplicates
{
	@BeforeClass
	public static void init()
	{
		TestsYamlCommon.init();
	}

	@Test
	public void testKeyOEWN()
	{
		LibTestModelDuplicates.testDuplicatesForKeyOEWN(TestsYamlCommon.model, TestsYamlCommon.ps);
	}

	@Test
	public void testKeyPos()
	{
		LibTestModelDuplicates.testDuplicatesForKeyPos(TestsYamlCommon.model, TestsYamlCommon.ps);
	}

	@Test
	public void testKeyIC()
	{
		LibTestModelDuplicates.testDuplicatesForKeyIC(TestsYamlCommon.model, TestsYamlCommon.ps);
	}

	@Test
	public void testKeyPWN()
	{
		LibTestModelDuplicates.testDuplicatesForKeyPWN(TestsYamlCommon.model, TestsYamlCommon.ps);
	}
}
