/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.yaml.in;

import org.junit.BeforeClass;
import org.junit.Test;
import org.oewntk.model.CoreModel;
import org.oewntk.model.LibTestModelKeys;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class TestsYamlModelKeys
{
	private static final String source = System.getProperty("SOURCE");

	// private static final String source2 = System.getProperty("SOURCE2");

	private static final PrintStream ps = !System.getProperties().containsKey("SILENT") ? System.out : new PrintStream(new OutputStream()
	{
		public void write(int b)
		{
			//DO NOTHING
		}
	});

	private static CoreModel model;

	@BeforeClass
	public static void init() throws IOException
	{
		File inDir = new File(source);
		// File inDir2 = new File(source2);

		model = Factory.makeCoreModel(inDir);
		System.err.println(model.info());
		System.err.println(model.counts());
	}

	@Test
	public void testEarthMulti()
	{
		LibTestModelKeys.testEarthMulti(model, ps);
	}

	@Test
	public void testEarthMono()
	{
		LibTestModelKeys.testEarthMono(model, ps);
	}

	@Test
	public void testBaroqueMulti()
	{
		LibTestModelKeys.testBaroqueMulti(model, ps);
	}

	@Test
	public void testBaroqueMono()
	{
		LibTestModelKeys.testBaroqueMono(model, ps);
	}

	@Test
	public void testMobile()
	{
		LibTestModelKeys.testMobile(model, ps);
	}

	@Test
	public void testBassDeep()
	{
		LibTestModelKeys.testBassDeep(model, ps);
	}

	@Test
	public void testBassShallow()
	{
		LibTestModelKeys.testBassShallow(model, ps);
	}

	@Test
	public void testRowDeep()
	{
		LibTestModelKeys.testRowDeep(model, ps);
	}

	@Test
	public void testRowShallow()
	{
		LibTestModelKeys.testRowShallow(model, ps);
	}

	@Test
	public void testCriticalDeep()
	{
		LibTestModelKeys.testCriticalDeep(model, ps);
	}

	@Test
	public void testCriticalPos()
	{
		LibTestModelKeys.testCriticalPos(model, ps);
	}

	@Test
	public void testCriticalPWN()
	{
		LibTestModelKeys.testCriticalPWN(model, ps);
	}
}
