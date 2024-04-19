package org.oewntk.yaml.`in`

import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*

/**
 * Abstract YAML processor
 *
 * @property dir dir containing YAML files
 * @param T type of elements to parse
 * @param K type of key
 * @param V type of value
 */
abstract class YamProcessor<T, K : Comparable<K>?, V>(@JvmField protected val dir: File) {

	/**
	 * YAML files to process
	 */
	protected abstract val files: Array<File>

	/**
	 * Process entry
	 *
	 * @param source entry source
	 * @param entry  key-value pair
	 * @return collection if items of type T
	 */
	protected abstract fun processEntry(source: String?, entry: Map.Entry<K, V>): Collection<T>?

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
				val processedItems = processEntry(file.name, entry)
				if (processedItems != null) {
					items.addAll(processedItems)
				}
			}
		}
	}
}
