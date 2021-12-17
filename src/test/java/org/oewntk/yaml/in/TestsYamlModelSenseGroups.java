/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.yaml.in;

import org.junit.BeforeClass;
import org.junit.Test;
import org.oewntk.model.LibTestModelSenseGroups;
import org.oewntk.model.Model;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class TestsYamlModelSenseGroups
{
	private static final String source = System.getProperty("SOURCE");

	private static final String source2 = System.getProperty("SOURCE2");

	private static final PrintStream ps = !System.getProperties().containsKey("SILENT") ? Tracing.psInfo : Tracing.psNull;

	private static Model model;

	@BeforeClass
	public static void init() throws IOException
	{
		File inDir = new File(source);
		File inDir2 = new File(source2);

		model = new Factory(inDir, inDir2).get();
		System.err.println(model.info());
		System.err.println(model.counts());
	}

	@Test
	public void testCISensesBaroque()
	{
		LibTestModelSenseGroups.testCISensesGroupingByLCLemma(model, "baroque", ps);
	}

	@Test
	public void testCISensesBaroqueA()
	{
		LibTestModelSenseGroups.testCISensesGroupingByLCLemmaAndPos(model, "baroque", 'a', ps);
	}

	@Test
	public void testCISensesCriticalA()
	{
		LibTestModelSenseGroups.testCISensesGroupingByLCLemmaAndPos(model, "critical", 'a', ps);
	}

	@Test
	public void testCISensesAiN()
	{
		LibTestModelSenseGroups.testCISensesGroupingByLCLemmaAndPos(model, "ai", 'n', ps);
	}

	@Test
	public void testCISensesAbsoluteA()
	{
		LibTestModelSenseGroups.testCISensesGroupingByLCLemmaAndPos(model, "absolute", 'a', ps);
	}
}
