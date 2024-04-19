/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*

/**
 * Abstract YAML processor, entry produces one item only of type T
 *
 * @param dir dir containing YAML files
 *
 * @param T type of elements to parse
 * @param K type of key
 * @param V type of value
 */
abstract class YamProcessor1<T, K : Comparable<K>?, V>(@JvmField protected var dir: File) {

	/**
	 * YAML files to process
	 */
	protected abstract val files: Array<File>

	/**
	 * Process entry
	 *
	 * @param source entry source
	 * @param entry  key-value pair
	 * @return item of type T
	 */
	protected abstract fun processEntry(source: String?, entry: Pair<K, V>): T?

	/**
	 * Parse
	 *
	 * @return collection of objects of type T
	 * @throws IOException io exception
	 */
	@Throws(IOException::class)
	fun parse(): Collection<T> {
		val items: MutableCollection<T> = ArrayList()
		for (file in files) {
			loadClass(file, items)
		}
		return Collections.unmodifiableCollection(items)
	}

	/**
	 * Load from file
	 *
	 * @param file  YAML file
	 * @param items accumulated items
	 * @throws IOException io exception
	 */
	@Throws(IOException::class)
	private fun loadClass(file: File, items: MutableCollection<T>) {
		val yaml = YamlUtils.newYaml()
		load(file, yaml, items)
	}

	/**
	 * Load from file
	 *
	 * @param file  YAML file
	 * @param yaml  Yaml
	 * @param items accumulated items
	 * @throws IOException io exception
	 */
	@Throws(IOException::class)
	private fun load(file: File, yaml: Yaml, items: MutableCollection<T>) {
		FileInputStream(file).use { inputStream ->
			val top: Map<K, V> = yaml.load(inputStream)
			for (entry in top.entries) {
				val t: T? = processEntry(file.name, entry.key to entry.value)
				if (t != null) {
					items.add(t)
				}
			}
		}
	}
}
