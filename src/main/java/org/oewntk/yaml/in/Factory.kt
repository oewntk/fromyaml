/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.yaml.in;

import org.oewntk.model.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.function.Supplier;

/**
 * Model factory
 */
public class Factory implements Supplier<Model>
{
	private final File inDir;

	private final File inDir2;

	/**
	 * Constructor
	 *
	 * @param inDir  dir containing release YAML files
	 * @param inDir2 dir containing extra YAML files
	 */
	public Factory(final File inDir, final File inDir2)
	{
		this.inDir = inDir;
		this.inDir2 = inDir2;
	}

	@Override
	public Model get()
	{
		CoreModel coreModel = new CoreFactory(inDir).get();
		if (coreModel == null)
		{
			return null;
		}

		try
		{
			// verb frames and templates
			Collection<VerbFrame> verbFrames = new VerbFrameProcessor(inDir).parse();
			Collection<VerbTemplate> verbTemplates = new VerbTemplateProcessor(inDir2).parse();
			Collection<Entry<String, int[]>> sensesToVerbTemplates = new SenseToVerbTemplatesProcessor(inDir2).parse();

			// tag counts
			Collection<Entry<String, TagCount>> sensesToTagCounts = new SenseToTagCountsProcessor(inDir2).parse();

			return new Model(coreModel, verbFrames, verbTemplates, sensesToVerbTemplates, sensesToTagCounts).setSources(inDir, inDir2);
		}
		catch (IOException e)
		{
			e.printStackTrace(Tracing.psErr);
			return null;
		}
	}

	/**
	 * Make model
	 *
	 * @param inDir  dir containing release YAML files
	 * @param inDir2 dir containing extra YAML files
	 * @return model
	 */
	static public Model makeModel(File inDir, File inDir2)
	{
		return new Factory(inDir, inDir2).get();
	}

	/**
	 * Make core model from YAML files
	 *
	 * @param args command-line arguments
	 * @return core model
	 */
	static public Model makeModel(String[] args)
	{
		File inDir = new File(args[0]);
		File inDir2 = new File(args[1]);
		return makeModel(inDir, inDir2);
	}

	/**
	 * Main
	 *
	 * @param args command-line arguments
	 */
	static public void main(String[] args)
	{
		Model model = makeModel(args);
		Tracing.psInfo.printf("[Model] %s%n%s%n%s%n", Arrays.toString(model.getSources()), model.info(), model.counts());
	}
}