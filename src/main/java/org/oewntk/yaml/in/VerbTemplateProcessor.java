/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.yaml.in;

import org.oewntk.model.VerbTemplate;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class VerbTemplateProcessor extends YamProcessor<VerbTemplate, Integer, String>
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

	static public void main(String[] args) throws IOException
	{
		String arg = args[0];
		System.out.println(arg);
		Map<Integer, VerbTemplate> map = new VerbTemplateProcessor(new File(arg)).process();
		for (int id : new int[]{120, 124})
		{
			//System.out.printf("%s%n", id);
			VerbTemplate verbTemplate = map.get(id);
			if (verbTemplate != null)
			{
				System.out.printf("%s%n", verbTemplate);
			}
			else
			{
				System.out.printf("%s not found%n", id);
			}
		}
	}
}
