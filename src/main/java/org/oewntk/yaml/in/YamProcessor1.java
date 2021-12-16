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
import java.util.*;

public abstract class YamProcessor1<T, K extends Comparable<K>, V>
{
	protected File dir;

	public YamProcessor1(final File dir)
	{
		this.dir = dir;
	}

	abstract protected File[] getFiles();

	abstract protected T processEntry(String source, Map.Entry<K, V> entry);

	public Collection<T> parse() throws IOException
	{
		Collection<T> items = new ArrayList<>();
		for (File file : getFiles())
		{
			loadClass(file, items);
		}
		return Collections.unmodifiableCollection(items);
	}

	private void loadClass(File file, Collection<T> items) throws IOException
	{
		Yaml yaml = new Yaml(new Constructor(TreeMap.class));
		load(file, yaml, items);
	}

	private void load(File file, Yaml yaml, Collection<T> items) throws IOException
	{
		try (InputStream inputStream = new FileInputStream(file))
		{
			Map<K, V> top = yaml.load(inputStream);
			for (Map.Entry<K, V> entry : top.entrySet())
			{
				T t = processEntry(file.getName(), entry);
				items.add(t);
			}
		}
	}
}
