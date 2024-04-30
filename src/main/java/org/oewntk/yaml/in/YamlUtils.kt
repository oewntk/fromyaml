/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml

/**
 * Yaml utilities
 *
 * @author Bernard Bou
 */
internal object YamlUtils {

    private const val CODEPOINT_LIMIT = 0x8000000

    /**
     * Avoid warning
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> safeCast(value: Any): T {
        return value as T
    }

    /**
     * Avoid warning
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Any?> safeNullableCast(value: Any?): T? {
        return value as T
    }

    /**
     * Build Yaml
     *
     * @return yaml
     */
    fun newYaml(): Yaml {
        //return new Yaml(new Constructor(TreeMap.class));

        val options = LoaderOptions()
        options.codePointLimit = CODEPOINT_LIMIT
        // println("codePointLimit=${options.codePointLimit}")
        return Yaml(options)
    }

    private val VOID_STRING_ARRAY = arrayOf<String>()

    /**
     * Assert keys are in set of valid keys
     *
     * @param source    source
     * @param keys      bunch of valid keys
     * @param validKeys array of extra valid keys
     */
    fun assertKeysIn(source: String, keys: Set<String>, vararg validKeys: String) {
        assertKeysIn(source, keys, VOID_STRING_ARRAY, *validKeys)
    }

    /**
     * Assert keys are in set of valid keys
     *
     * @param source     source
     * @param keys       bunch of valid keys
     * @param validKeys1 array of extra valid keys
     * @param validKeys2 array of extra valid keys
     */
    fun assertKeysIn(source: String, keys: Set<String>, validKeys1: Array<String>, vararg validKeys2: String) {
        val validKeys = mutableListOf(*validKeys1, *validKeys2)
        for (key in keys) {
            var valid = false
            for (validKey in validKeys) {
                if (key == validKey) {
                    valid = true
                    break
                }
            }
            require(valid) { "'$key' in $source" }
        }
    }

    /**
     * Dump map
     *
     * @param format format string for key-value pair
     * @param map    string to object map
     */
    fun dumpMap(format: String, map: Map<String, *>) {
        for ((k, v) in map) {
            Tracing.psInfo.printf(format, k, v.toString())
        }
        Tracing.psInfo.println()
    }
}
