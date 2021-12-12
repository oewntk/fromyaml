/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.yaml.in;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SenseToVerbTemplatesProcessor extends YamProcessor<int[], String, List<Integer>>
{
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
	protected int[] processEntry(String source, Map.Entry<String, List<Integer>> entry)
	{
		boolean dump = false;

		String sensekey = entry.getKey();
		List<Integer> v = entry.getValue();
		if (dump)
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
		return templateIds;
	}

	static public void main(String[] args) throws IOException
	{
		String arg = args[0];
		System.out.println(arg);
		Map<String, int[]> map = new SenseToVerbTemplatesProcessor(new File(arg)).parse();
		for (String id : new String[]{"abide%2:31:00::", "abominate%2:37:00::", "abound%2:42:01::", "pet%2:35:00::", "pet%2:99:00::"})
		{
			//System.out.printf("%s%n", id);
			int[] verbTemplateIds = map.get(id);
			if (verbTemplateIds != null)
			{
				System.out.printf("%s %s%n", id, Arrays.toString(verbTemplateIds));
			}
			else
			{
				System.out.printf("%s not found%n", id);
			}
		}
	}
}
