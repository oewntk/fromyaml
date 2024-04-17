/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.yaml.in;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

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

	static private final int CODEPOINT_LIMIT = 0x8000000;

	/**
	 * Build Yaml
	 *
	 * @return yaml
	 */
	static Yaml newYaml()
	{
		//return new Yaml(new Constructor(TreeMap.class));

		LoaderOptions options = new LoaderOptions();
		options.setCodePointLimit(CODEPOINT_LIMIT);
		// int limit = options.getCodePointLimit();
		// System.out.println("codePointLimit=" + limit);
		return new Yaml(options);
	}

	private static final String[] VOID_STRING_ARRAY = new String[0];

	/**
	 * Assert keys are in set of valid keys
	 *
	 * @param source    source
	 * @param keys      bunch of valid keys
	 * @param validKeys array of extra valid keys
	 */
	static void assertKeysIn(String source, Set<String> keys, String... validKeys)
	{
		assertKeysIn(source, keys, VOID_STRING_ARRAY, validKeys);
	}

	/**
	 * Assert keys are in set of valid keys
	 *
	 * @param source     source
	 * @param keys       bunch of valid keys
	 * @param validKeys1 array of extra valid keys
	 * @param validKeys2 array of extra valid keys
	 */
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
				//  System.out.println("\"" + key + "\" in " + source);
				throw new IllegalArgumentException("'" + key + "' in " + source);
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
