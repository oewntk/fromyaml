/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.yaml.in;

import org.oewntk.model.TagCount;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class SenseToTagCountsProcessor extends YamProcessor<TagCount, String, Map<String, Integer>>
{
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
	protected TagCount processEntry(String source, Map.Entry<String, Map<String, Integer>> entry)
	{
		boolean dump = false;

		String id = entry.getKey();
		Map<String, Integer> tagCntMap = entry.getValue();
		YamlUtils.assertKeysIn(source, tagCntMap.keySet(), KEY_TAGCOUNT_SENSE_NUM, KEY_TAGCOUNT_COUNT);
		if (dump)
		{
			System.out.println(id);
		}
		int senseNum = tagCntMap.get(KEY_TAGCOUNT_SENSE_NUM);
		int count = tagCntMap.get(KEY_TAGCOUNT_COUNT);
		return new TagCount(senseNum, count);
	}

	static public void main(String[] args) throws IOException
	{
		String arg = args[0];
		System.out.println(arg);
		Map<String, TagCount> map = new SenseToTagCountsProcessor(new File(arg)).parse();
		for (String id : new String[]{"abandon%1:07:00::", "abandon%2:31:00::", "abandon%2:31:01::", "abandon%2:38:00::", "abandon%2:40:00::", "abandon%2:40:01::"})
		{
			//System.out.printf("%s%n", id);
			TagCount tagCount = map.get(id);
			if (tagCount != null)
			{
				System.out.printf("%s %s%n", id, tagCount);
			}
			else
			{
				System.out.printf("%s not found%n", id);
			}
		}

		/*
		sense_key  sense_number  tag_cnt
		sense_number is a decimal integer indicating the sense number of the word, within the part of speech encoded in sense_key, in the WordNet database
		abandon%1:07:00:: 1 4
		abandon%2:31:00:: 5 3
		abandon%2:31:01:: 4 5
		abandon%2:38:00:: 3 6
		abandon%2:40:00:: 1 10
		abandon%2:40:01:: 2 6
		*/
	}
}
