/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.yaml.in;

import org.junit.Assert;
import org.oewntk.model.CoreModel;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;

public class TestsYamlCommon
{
	private static final String source = System.getProperty("SOURCE");

	static final PrintStream ps = !System.getProperties().containsKey("SILENT") ? System.out : new PrintStream(new OutputStream()
	{
		public void write(int b)
		{
			//DO NOTHING
		}
	});

	static CoreModel model = null;

	public static void init()
	{
		if (model == null)
		{
			if (source == null)
			{
				Tracing.psErr.println("Define YAML source dir with -DSOURCE=path%n");
				Tracing.psErr.println("When running Maven tests, define the yaml directory as child to the project directory.");
				Assert.fail();
			}
			File inDir = new File(source);
			Tracing.psInfo.printf("source=%s%n", inDir.getAbsolutePath());
			if (!inDir.exists())
			{
				Tracing.psErr.println("Define YAML source dir that exists");
				Assert.fail();
			}

			model = new CoreFactory(inDir).get();
		}
		ps.println(model.info());
		ps.println(model.counts());
	}
}
