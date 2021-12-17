/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.yaml.in;

import java.util.*;

/**
 * Yaml utilities
 *
 * @author Bernard Bou
 */
class YamlUtils
{
	private YamlUtils()
	{
	}
	private static final String[] VOID_STRING_ARRAY = new String[0];

	static void assertKeysIn(String source, Set<String> keys, String... validKeys)
	{
		assertKeysIn(source, keys, VOID_STRING_ARRAY, validKeys);
	}
	static void assertKeysIn(String source, Set<String> keys, String[] validKeys1, String... validKeys2)
	{
		List<String> validKeys = new ArrayList<>();
		Collections.addAll(validKeys, validKeys1);
		Collections.addAll(validKeys, validKeys2);
		for (String key : keys)
		{
			boolean valid = false;
			for (String validKey : validKeys)
			{
				if (key.equals(validKey))
				{
					valid = true;
					break;
				}
			}
			if (!valid)
			{
				throw new IllegalArgumentException(key + " " + source);
			}
		}
	}

	/**
	 * Dump map
	 *
	 * @param format format string for key-value pair
	 * @param map    string to object map
	 */
	static void dumpMap(String format, Map<String, Object> map)
	{
		for (Map.Entry<String, Object> entry3 : map.entrySet())
		{
			String k = entry3.getKey();
			Object v = entry3.getValue();
			Tracing.psInfo.printf(format, k, v.toString());
		}
		Tracing.psInfo.println();
	}
}
