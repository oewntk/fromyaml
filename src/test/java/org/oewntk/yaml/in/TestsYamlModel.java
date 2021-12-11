/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.yaml.in;

import org.junit.BeforeClass;
import org.junit.Test;
import org.oewntk.model.LibTestModel;
import org.oewntk.model.Model;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Set;

public class TestsYamlModel
{
	private static final String source = System.getProperty("SOURCE");

	private static final String source2 = System.getProperty("SOURCE2");

	private static final PrintStream ps = !System.getProperties().containsKey("SILENT") ? System.out : new PrintStream(new OutputStream()
	{
		public void write(int b)
		{
			//DO NOTHING
		}
	});

	private static final boolean peekTestWords = false;

	private static final Set<String> testWords = Set.of("baroque", "Baroque", "bass", "row");

	private static Model model;

	@BeforeClass
	public static void init() throws IOException
	{
		File inDir = new File(source);
		File inDir2 = new File(source2);

		model = Factory.makeModel(inDir, inDir2);
		System.err.println(model.info());
		System.err.println(model.counts());
	}

	@Test
	public void testKey()
	{
		LibTestModel.testKey(model, ps);
	}

	@Test
	public void testKeyPos()
	{
		LibTestModel.testKeyPos(model, ps);
	}

	@Test
	public void testKeyIC()
	{
		LibTestModel.testKeyIC(model, ps);
	}

	@Test
	public void testKeyPWN()
	{
		LibTestModel.testKeyPWN(model, ps);
	}

	@Test
	public void testStreams1()
	{
		LibTestModel.testStreams(model, LibTestModel::makeMap, testWords, peekTestWords, ps);
	}

	@Test
	public void testStreams2()
	{
		LibTestModel.testStreams(model, LibTestModel::makeSortedMap, testWords, peekTestWords, ps);
	}

	@Test
	public void testBass()
	{
		LibTestModel.testWord("bass", model, ps);
	}

	@Test
	public void testRow()
	{
		LibTestModel.testWord("row", model, ps);
	}

	@Test
	public void testBaroque()
	{
		LibTestModel.testWords(model, ps, "baroque", "Baroque");
	}

	@Test
	public void testEarth()
	{
		LibTestModel.testWords(model, ps, "earth", "Earth");
	}

	@Test
	public void testCritical()
	{
		LibTestModel.testWord("critical", 'a', model, ps);
	}

	@Test
	public void testHollywood()
	{
		LibTestModel.testWord("Hollywood", 'a', model, ps);
	}

	@Test
	public void testVictorian()
	{
		LibTestModel.testWord("Victorian", 'a', model, ps);
	}

	@Test
	public void testAllied()
	{
		LibTestModel.testWord("allied", 'a', model, ps);
	}

	@Test
	public void testAlliedUpper()
	{
		LibTestModel.testWord("Allied", 'a', model, ps);
	}

	@Test
	public void testAbsent()
	{
		LibTestModel.testWord("absent", 'a', model, ps);
	}

	@Test
	public void testApocryphal()
	{
		LibTestModel.testWord("apocryphal", 'a', model, ps);
	}

	@Test
	public void testUsed()
	{
		LibTestModel.testWord("used", 'a', model, ps);
	}
}
