/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.yaml.in;

import java.io.File;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SenseToVerbTemplatesProcessor extends YamProcessor1<Entry<String, int[]>, String, List<Integer>>
{
	static private final boolean DUMP = false;

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
	protected Entry<String, int[]> processEntry(String source, Map.Entry<String, List<Integer>> entry)
	{
		String sensekey = entry.getKey();
		List<Integer> v = entry.getValue();
		if (DUMP)
		{
			System.out.println(sensekey);
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
