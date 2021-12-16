/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.yaml.in;

import org.oewntk.model.VerbTemplate;

import java.io.File;
import java.util.Map;

public class VerbTemplateProcessor extends YamProcessor1<VerbTemplate, Integer, String>
{
	public VerbTemplateProcessor(final File dir)
	{
		super(dir);
		this.dir = dir;
	}

	@Override
	protected File[] getFiles()
	{
		return dir.listFiles((f) -> f.getName().matches("verbTemplates.yaml"));
	}

	@Override
	protected VerbTemplate processEntry(String source, Map.Entry<Integer, String> entry)
	{
		boolean dump = false;

		Integer id = entry.getKey();
		String v = entry.getValue();
		if (dump)
		{
			System.out.println(id);
		}
		return new VerbTemplate(id, v);
	}
}
