/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.error.YAMLException

/**
 * Yaml utilities
 *
 * @author Bernard Bou
 */
@Suppress("KotlinConstantConditions")
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

    // examples

    /**
     * Process examples
     *
     * @param examples YAML objects for examples
     * @param keyText YAML key for text
     * @param keySource YAML key for source
     * @return list of pairs of processed text, source
     */
    fun processExamples(examples: List<*>?, keyText: String, keySource: String): List<Pair<String, String?>>? {
        return examples
            ?.asSequence()
            ?.map { processExample(it, keyText, keySource) }
            ?.toList()
    }

    /**
     * Process example
     *
     * @param example YAML object
     * @param keyText YAML key for text
     * @param keySource YAML key for source
     * @return pair of processed text, source
     */
    fun processExample(example: Any?, keyText: String, keySource: String): Pair<String, String?> {
        return when (example) {
            is String    -> {
                processExampleText(example) to null
            }

            is Map<*, *> -> {
                val exampleMap: Map<String, *> = safeCast(example)
                assertKeysIn(exampleMap.keys, keyText, keySource)
                val text = exampleMap[keyText].toString()
                val source = exampleMap[keySource].toString()
                processExampleText(text) to source
            }

            else         -> throw YAMLException(example.toString())
        }
    }

    private const val EXAMPLES_FIX_QUOTES = true

    private const val LEGACY_V1 = true

    @Suppress("KotlinConstantConditions")
    private const val EXAMPLES_TRIM = !LEGACY_V1

    /**
     * Process example text
     *
     * @param example text
     * @return processed text
     */
    fun processExampleText(example: String): String {
        val trimmed = if (EXAMPLES_TRIM) example.trim() else example
        if (trimmed.matches("\".*\"".toRegex())) {
            Tracing.psErr.println("[W] Illegal quoted example format <$example>")
            return if (!EXAMPLES_FIX_QUOTES) example else example.trim('"')
        }
        if (trimmed.contains('"')) {
            val quoteCount = countQuotes(trimmed)
            if (quoteCount % 2 != 0)
                Tracing.psErr.println("[W] Uneven quotes $quoteCount in <$example>")
            // else
            //  Tracing.psInfo.println("[W] With-quotes example format <$example>")
        }
        return trimmed
    }

    /**
     * Count quotes in string
     * @param str string
     * @return number of quotes in str
     */
    private fun countQuotes(str: String): Int {
        var quoteCount = 0
        var p = -1
        while ((str.indexOf('"', p + 1).also { p = it }) != -1) {
            quoteCount++
        }
        return quoteCount
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
