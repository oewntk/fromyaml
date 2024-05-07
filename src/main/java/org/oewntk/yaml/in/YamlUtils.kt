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

    /**
     * Assert keys are in set of valid keys
     *
     * @param keys      bunch of valid keys
     * @param validKeys array of extra valid keys
     */
    fun assertKeysIn(keys: Set<String>, vararg validKeys: String) {
        assertKeysIn(keys, arrayOf(), *validKeys)
    }

    /**
     * Assert keys are in set of valid keys
     *
     * @param keys       bunch of valid keys
     * @param validKeys1 array of extra valid keys
     * @param validKeys2 array of extra valid keys
     */
    fun assertKeysIn(keys: Set<String>, validKeys1: Array<String>, vararg validKeys2: String) {
        val validKeys = setOf(*validKeys1, *validKeys2)
        val difference = keys.subtract(validKeys)
        if (difference.isNotEmpty()) {
            val rogues = difference.joinToString { "'$it'" }
            throw IllegalArgumentException(rogues)
        }
    }
}

/**
 * Dump map
 *
 * @param map    string to object map
 * @param indent indent
 */
fun dumpMap(map: Map<String, *>, indent: String = "") {
    val s = map
        .entries
        .joinToString(postfix = "\n") { "$indent${it.key} ${it.value}" }
    Tracing.psInfo.println(s)
}
