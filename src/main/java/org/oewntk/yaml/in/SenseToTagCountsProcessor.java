/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.yaml.in;

import org.oewntk.model.TagCount;

import java.io.File;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Map.Entry;

public class SenseToTagCountsProcessor extends YamProcessor1<Entry<String, TagCount>, String, Map<String, Integer>>
{
	private static final boolean DUMP = false;

	private static final String KEY_TAGCOUNT_SENSE_NUM = "num";

	private static final String KEY_TAGCOUNT_COUNT = "cnt";

	public SenseToTagCountsProcessor(final File dir)
	{
		super(dir);
		this.dir = dir;
	}

	@Override
	protected File[] getFiles()
	{
		return dir.listFiles((f) -> f.getName().matches("senseToTagCounts.yaml"));
	}

	@Override
	protected Entry<String, TagCount> processEntry(String source, Map.Entry<String, Map<String, Integer>> entry)
	{
		String sensekey = entry.getKey();
		Map<String, Integer> tagCntMap = entry.getValue();
		YamlUtils.assertKeysIn(source, tagCntMap.keySet(), KEY_TAGCOUNT_SENSE_NUM, KEY_TAGCOUNT_COUNT);
		if (DUMP)
		{
			Tracing.psInfo.println(sensekey);
		}
		int senseNum = tagCntMap.get(KEY_TAGCOUNT_SENSE_NUM);
		int count = tagCntMap.get(KEY_TAGCOUNT_COUNT);
		return new SimpleEntry<>(sensekey, new TagCount(senseNum, count));
	}
}
