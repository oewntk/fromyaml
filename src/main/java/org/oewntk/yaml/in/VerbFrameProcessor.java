/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.yaml.in;

import org.oewntk.model.VerbFrame;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class VerbFrameProcessor extends YamProcessor1<VerbFrame, String, String>
{
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
	protected VerbFrame processEntry(String source, Map.Entry<String, String> entry)
	{
		boolean dump = false;

		String id = entry.getKey();
		String v = entry.getValue();
		if (dump)
		{
			System.out.println(id);
		}
		return new VerbFrame(id, v);
	}
}
