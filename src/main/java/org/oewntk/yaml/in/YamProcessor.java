/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.yaml.in;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public abstract class YamProcessor<T,K extends Comparable<K>,V>
{
	protected File dir;

	public YamProcessor(final File dir)
	{
		this.dir = dir;
	}

	abstract protected File[] getFiles();

	abstract protected T processEntry(String source, Map.Entry<K, V> entry);

	public Map <K,T> parse() throws IOException
	{
		Map <K,T> map = new TreeMap<>();
		for (File file : getFiles())
		{
			loadClass(file, map);
		}
		return Collections.unmodifiableMap(map);
	}

	private void loadClass(File file, Map <K,T> map) throws IOException
	{
		Yaml yaml = new Yaml(new Constructor(TreeMap.class));
		load(file, yaml, map);
	}

	private void load(File file, Yaml yaml, Map <K,T> map) throws IOException
	{
		try (InputStream inputStream = new FileInputStream(file))
		{
			Map<K, V> top = yaml.load(inputStream);
			for (Map.Entry<K, V> entry : top.entrySet())
			{
				T t = processEntry(file.getName(), entry);
				map.put(entry.getKey(), t);
			}
		}
	}
}
