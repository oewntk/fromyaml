/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.yaml.in;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Abstract YAML processor, entry produces one item only of type T
 *
 * @param <T> type of elements to parse
 * @param <K> type of key
 * @param <V> type of value
 */
public abstract class YamProcessor1<T, K extends Comparable<K>, V>
{
	protected File dir;

	/**
	 * Constructor
	 *
	 * @param dir dir containing YAML files
	 */
	public YamProcessor1(final File dir)
	{
		this.dir = dir;
	}

	/**
	 * Files to process
	 *
	 * @return YAML files to process
	 */
	abstract protected File[] getFiles();

	/**
	 * Process entry
	 *
	 * @param source entry source
	 * @param entry  key-value pair
	 * @return item of type T
	 */
	abstract protected T processEntry(String source, Map.Entry<K, V> entry);

	/**
	 * Parse
	 *
	 * @return collection of objects of type T
	 * @throws IOException io exception
	 */
	public Collection<T> parse() throws IOException
	{
		Collection<T> items = new ArrayList<>();
		for (File file : getFiles())
		{
			loadClass(file, items);
		}
		return Collections.unmodifiableCollection(items);
	}

	/**
	 * Load from file
	 *
	 * @param file  YAML file
	 * @param items accumulated items
	 * @throws IOException io exception
	 */
	private void loadClass(File file, Collection<T> items) throws IOException
	{
		Yaml yaml = YamlUtils.newYaml();
		load(file, yaml, items);
	}

	/**
	 * Load from file
	 *
	 * @param file  YAML file
	 * @param yaml  Yaml
	 * @param items accumulated items
	 * @throws IOException io exception
	 */
	private void load(File file, Yaml yaml, Collection<T> items) throws IOException
	{
		try (InputStream inputStream = new FileInputStream(file))
		{
			Map<K, V> top = yaml.load(inputStream);
			for (Map.Entry<K, V> entry : top.entrySet())
			{
				T t = processEntry(file.getName(), entry);
				if (t != null)
				{
					items.add(t);
				}
			}
		}
	}
}
