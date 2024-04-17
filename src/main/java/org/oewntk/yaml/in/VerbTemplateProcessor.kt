/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.yaml.in;

import org.oewntk.model.VerbTemplate;

import java.io.File;
import java.util.Map.Entry;

public class VerbTemplateProcessor extends YamProcessor1<VerbTemplate, Integer, String>
{
	private static final boolean DUMP = false;

	/**
	 * Constructor
	 *
	 * @param dir dir containing YAML files
	 */
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
	protected VerbTemplate processEntry(final String source, final Entry<Integer, String> entry)
	{
		Integer id = entry.getKey();
		String v = entry.getValue();
		if (DUMP)
		{
			Tracing.psInfo.println(id);
		}
		return new VerbTemplate(id, v);
	}
}
