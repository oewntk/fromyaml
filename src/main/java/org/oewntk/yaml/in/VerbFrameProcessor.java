/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.yaml.in;

import org.oewntk.model.VerbFrame;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class VerbFrameProcessor extends YamProcessor<VerbFrame, String, String>
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

	static public void main(String[] args) throws IOException
	{
		String arg = args[0];
		System.out.println(arg);
		Map<String, VerbFrame> map = new VerbFrameProcessor(new File(arg)).parse();
		for (String id : new String[]{"via", "vtii-adj"})
		{
			//System.out.printf("%s%n", id);
			VerbFrame verbFrame = map.get(id);
			if (verbFrame != null)
			{
				System.out.printf("%s%n", verbFrame);
			}
			else
			{
				System.out.printf("%s not found%n", id);
			}
		}
	}
}
