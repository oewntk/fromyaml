/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.yaml.in;

import org.junit.BeforeClass;
import org.junit.Test;
import org.oewntk.model.Key;
import org.oewntk.model.LibTestModel;

import java.util.Set;

public class TestsYamlModelWords
{
	private static final boolean peekTestWords = false;

	private static final Set<String> testWords = Set.of("baroque", "Baroque", "allied", "Allied", "earth", "Earth", "bass", "row", "critical");

	@BeforeClass
	public static void init()
	{
		TestsYamlCommon.init();
	}

	@Test
	public void testScanLexesForTestWords()
	{
		LibTestModel.testScanLexesForTestWords(TestsYamlCommon.model, Key.OEWN::of, LibTestModel::makeIndexMap, testWords, peekTestWords, TestsYamlCommon.ps);
	}

	@Test
	public void testScanLexesForTestWordsSorted()
	{
		LibTestModel.testScanLexesForTestWords(TestsYamlCommon.model,  Key.OEWN::of, LibTestModel::makeSortedIndexMap, testWords, peekTestWords, TestsYamlCommon.ps);
	}

	@Test
	public void testBass()
	{
		LibTestModel.testWord("bass", TestsYamlCommon.model, TestsYamlCommon.ps);
	}

	@Test
	public void testRow()
	{
		LibTestModel.testWord("row", TestsYamlCommon.model, TestsYamlCommon.ps);
	}

	@Test
	public void testBaroque()
	{
		LibTestModel.testWords(TestsYamlCommon.model, TestsYamlCommon.ps, "baroque", "Baroque");
	}

	@Test
	public void testEarth()
	{
		LibTestModel.testWords(TestsYamlCommon.model, TestsYamlCommon.ps, "earth", "Earth");
	}

	@Test
	public void testCritical()
	{
		LibTestModel.testWord("critical", 'a', TestsYamlCommon.model, TestsYamlCommon.ps);
	}

	@Test
	public void testHollywood()
	{
		LibTestModel.testWord("Hollywood", 'a', TestsYamlCommon.model, TestsYamlCommon.ps);
	}

	@Test
	public void testVictorian()
	{
		LibTestModel.testWord("Victorian", 'a', TestsYamlCommon.model, TestsYamlCommon.ps);
	}

	@Test
	public void testAllied()
	{
		LibTestModel.testWord("allied", 'a', TestsYamlCommon.model, TestsYamlCommon.ps);
	}

	@Test
	public void testAlliedUpper()
	{
		LibTestModel.testWord("Allied", 'a', TestsYamlCommon.model, TestsYamlCommon.ps);
	}

	@Test
	public void testAbsent()
	{
		LibTestModel.testWord("absent", 'a', TestsYamlCommon.model, TestsYamlCommon.ps);
	}

	@Test
	public void testApocryphal()
	{
		LibTestModel.testWord("apocryphal", 'a', TestsYamlCommon.model, TestsYamlCommon.ps);
	}

	@Test
	public void testUsed()
	{
		LibTestModel.testWord("used", 'a', TestsYamlCommon.model, TestsYamlCommon.ps);
	}
}
