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
import java.util.Map.Entry;

/**
 * Abstract YAML processor
 *
 * @param <T> type of elements to parse
 * @param <K> type of key
 * @param <V> type of value
 */
public abstract class YamProcessor<T, K extends Comparable<K>, V>
{
	protected final File dir;

	/**
	 * Constructor
	 *
	 * @param dir dir containing YAML files
	 */
	public YamProcessor(final File dir)
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
	 * @return collection if items of type T
	 */
	abstract protected Collection<T> processEntry(final String source, final Entry<K, V> entry);

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
	private void loadClass(final File file, final Collection<T> items) throws IOException
	{
		Yaml yaml = new Yaml(new Constructor(TreeMap.class));
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
	private void load(final File file, final Yaml yaml, final Collection<T> items) throws IOException
	{
		try (InputStream inputStream = new FileInputStream(file))
		{
			Map<K, V> top = yaml.load(inputStream);
			for (Entry<K, V> entry : top.entrySet())
			{
				Collection<T> processedItems = processEntry(file.getName(), entry);
				if (processedItems != null)
				{
					items.addAll(processedItems);
				}
			}
		}
	}
}
