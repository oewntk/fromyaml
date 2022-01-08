/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.yaml.in;

import org.oewntk.model.CoreModel;

import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;

/**
 * Core model factory
 */
public class CoreFactory implements Supplier<CoreModel>
{
	private final File inDir;

	/**
	 * Constructor
	 *
	 * @param inDir dir containing YAML files
	 */
	public CoreFactory(final File inDir)
	{
		this.inDir = inDir;
	}

	@Override
	public CoreModel get()
	{
		try
		{
			return new Parser(inDir) //
					.parse() //
					.generateInverseRelations() //
					.setSource(inDir);
		}
		catch (IOException e)
		{
			e.printStackTrace(Tracing.psErr);
			return null;
		}
	}

	/**
	 * Make core model from YAML files
	 *
	 * @param args command-line arguments
	 * @return core model
	 */
	static public CoreModel makeCoreModel(String[] args)
	{
		File inDir = new File(args[0]);
		return new CoreFactory(inDir).get();
	}

	/**
	 * Main
	 *
	 * @param args command-line arguments
	 */
	static public void main(String[] args)
	{
		CoreModel model = makeCoreModel(args);
		Tracing.psInfo.printf("[CoreModel] %s%n%s%n%s%n", model.getSource(), model.info(), model.counts());
	}
}