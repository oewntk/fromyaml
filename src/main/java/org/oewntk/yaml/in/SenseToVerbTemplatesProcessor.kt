/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.yaml.in;

import java.io.File;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map.Entry;

/**
 * Sense-to-verb-templates processor
 */
public class SenseToVerbTemplatesProcessor extends YamProcessor1<Entry<String, int[]>, String, List<Integer>>
{
	private static final boolean DUMP = false;

	/**
	 * Constructor
	 *
	 * @param dir dir containing YAML files
	 */
	public SenseToVerbTemplatesProcessor(final File dir)
	{
		super(dir);
		this.dir = dir;
	}

	@Override
	protected File[] getFiles()
	{
		return dir.listFiles((f) -> f.getName().matches("senseToVerbTemplates.yaml"));
	}

	@Override
	protected Entry<String, int[]> processEntry(final String source, final Entry<String, List<Integer>> entry)
	{
		String sensekey = entry.getKey();
		List<Integer> v = entry.getValue();
		if (DUMP)
		{
			Tracing.psInfo.println(sensekey);
		}
		int n = v.size();
		if (n == 0)
		{
			return null;
		}
		int[] templateIds = new int[n];
		int i = 0;
		for (Integer templateId : v)
		{
			templateIds[i++] = templateId;
		}
		return new SimpleEntry<>(sensekey, templateIds);
	}
}
