/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.yaml.in;

import org.oewntk.model.VerbFrame;

import java.io.File;
import java.util.Map.Entry;

/**
 * Verb frames parser
 */
public class VerbFrameProcessor extends YamProcessor1<VerbFrame, String, String>
{
	private static final boolean DUMP = false;

	/**
	 * Constructor
	 * @param dir dir containing YAML files
	 */
	public VerbFrameProcessor(final File dir)
	{
		super(dir);
		this.dir = dir;
	}

	@Override
	protected File[] getFiles()
	{
		return dir.listFiles((f) -> f.getName().matches("frames.yaml"));
	}

	@Override
	protected VerbFrame processEntry(final String source, final Entry<String, String> entry)
	{
		String id = entry.getKey();
		String v = entry.getValue();
		if (DUMP)
		{
			Tracing.psInfo.println(id);
		}
		return new VerbFrame(id, v);
	}
}
