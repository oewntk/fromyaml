/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.oewntk.model.*
import org.oewntk.yaml.`in`.YamlUtils.assertKeysIn
import org.oewntk.yaml.`in`.YamlUtils.processExamples
import org.oewntk.yaml.`in`.YamlUtils.safeCast
import org.oewntk.yaml.`in`.YamlUtils.safeNullableCast
import java.io.File

/**
 * Lex YAML parser
 *
 * @property dir dir containing YAML files
 */
class LexParser(dir: File) : YamProcessor<Lex, String, Map<String, *>>(dir) {

    /**
     * Accumulated senses as lexes are processed
     */
    val senses: MutableList<Sense> = ArrayList()

    override val files: Array<File>
        get() = dir.listFiles { f: File -> f.name.matches("entries.*\\.yaml".toRegex()) }!!

    override fun processEntry(source: String?, entry: Pair<String, Map<String, *>>): Collection<Lex> {
        try {
            val lexes: MutableList<Lex> = ArrayList()

            val lemma = entry.first
            val lemmaMap = entry.second
            for ((type, value1) in lemmaMap) {
                val lexMap: Map<String, *> = safeCast(value1!!)
                assertKeysIn(lexMap.keys, KEY_LEX_SENSE, KEY_LEX_PRONUNCIATION, KEY_LEX_FORM)
                if (DUMP) {
                    dumpMap(lexMap)
                }

                // lex
                val lex = Lex(lemma, type, source = source)

                // senses
                val senseMaps: List<Map<String, *>> = safeCast(lexMap[KEY_LEX_SENSE]!!)
                val lexSenses = MutableList(senseMaps.size) {
                    val senseMap = senseMaps[it]
                    assertKeysIn(
                        senseMap.keys,
                        VALID_SENSE_RELATIONS,
                        KEY_SENSE_ID,
                        KEY_SENSE_SYNSET,
                        KEY_SENSE_VERBFRAMES,
                        KEY_SENSE_VERBFRAMES,
                        KEY_SENSE_ADJPOSITION,
                        KEY_SENSE_EXAMPLES
                    )
                    if (DUMP) {
                        dumpMap(senseMap, indent = "\t")
                    }

                    val senseId = senseMap[KEY_SENSE_ID]!! as SenseKey
                    val synsetId = senseMap[KEY_SENSE_SYNSET]!! as SynsetId
                    val examples: List<Pair<String, String?>>? = processExamples(safeNullableCast(senseMap[KEY_SENSE_EXAMPLES]), KEY_EXAMPLE_TEXT, KEY_EXAMPLE_SOURCE)
                    val verbFrames: List<String>? = safeNullableCast(senseMap[KEY_SENSE_VERBFRAMES])
                    val adjPosition = senseMap[KEY_SENSE_ADJPOSITION] as String?

                    // relations
                    val relations = SENSE_RELATIONS
                        .asSequence()
                        .filter { relation -> senseMap.containsKey(relation) }
                        .map { relation -> relation to safeCast<List<String>>(senseMap[relation]!!).toSet() } // relation, setOf(targets)
                        .toMap()
                        .ifEmpty { null }

                    // sense
                    val lexSense = Sense(senseId, lex, type[0], it, synsetId, examples?.toTypedArray(), verbFrames?.toTypedArray(), adjPosition, relations)
                    senses.add(lexSense)
                    lexSense
                }
                lex.senseKeys = lex.senseKeys.toMutableList() + lexSenses.map { it.senseKey }.asSequence()

                // pronunciations
                val pronunciationList: List<Map<String, *>>? = safeNullableCast(lexMap[KEY_LEX_PRONUNCIATION])
                val pronunciations: Array<Pronunciation>? = if (pronunciationList != null) {
                    Array(pronunciationList.size) {
                        val pronunciationMap = pronunciationList[it]
                        assertKeysIn(pronunciationMap.keys, KEY_PRONUNCIATION_VARIETY, KEY_PRONUNCIATION_VALUE)
                        if (DUMP) {
                            dumpMap(pronunciationMap, indent = "\t")
                        }
                        val variety = pronunciationMap[KEY_PRONUNCIATION_VARIETY] as String?
                        val value = pronunciationMap[KEY_PRONUNCIATION_VALUE] as String
                        Pronunciation(value, variety)
                    }
                } else null
                lex.pronunciations = pronunciations?.toSet()

                // forms
                val forms: List<String>? = safeNullableCast(lexMap[KEY_LEX_FORM])
                lex.forms = forms?.toSet() // preserves order (LinkedHashSet)

                // accumulate
                lexes.add(lex)
            }
            return lexes

        } catch (iae: IllegalArgumentException) {
            throw IllegalArgumentException("${iae.message} in $source")
        }
    }

    companion object {

        private const val DUMP = false

        private const val KEY_LEX_SENSE = "sense"
        private const val KEY_LEX_PRONUNCIATION = "pronunciation"
        private const val KEY_LEX_FORM = "form"

        private const val KEY_SENSE_ID = "id"
        private const val KEY_SENSE_SYNSET = "synset"
        private const val KEY_SENSE_ADJPOSITION = "adjposition"
        private const val KEY_SENSE_VERBFRAMES = "subcat"
        private const val KEY_SENSE_EXAMPLES = "sent"

        private const val KEY_EXAMPLE_SOURCE = "source"
        private const val KEY_EXAMPLE_TEXT = "text"

        private const val KEY_PRONUNCIATION_VARIETY = "variety"
        private const val KEY_PRONUNCIATION_VALUE = "value"

        private val VALID_SENSE_RELATIONS = arrayOf(
            "antonym",
            "similar",
            "exemplifies",
            "derivation",
            "pertainym",
            "participle",
            "also",
            "domain_region",
            "domain_topic",

            "state",
            "result",
            "event",
            "property",
            "location",
            "destination",
            "agent",
            "undergoer",
            "uses",
            "instrument",
            "by_means_of",
            "material",
            "vehicle",
            "body_part",

            "collocation",
            "other"
        )

        private val SENSE_RELATIONS = arrayOf(
            "antonym",
            "similar",
            "exemplifies", "is_exemplified_by",
            "derivation",
            "pertainym",
            "participle",
            "also",
            "domain_region", "has_domain_region",
            "domain_topic", "has_domain_topic",

            "agent",
            "material",
            "event",
            "instrument",
            "location",
            "by_means_of",
            "undergoer",
            "property",
            "result",
            "state",
            "uses",
            "destination",
            "body_part",
            "vehicle",

            "collocation",
            "other"
        )

    /*
    ignored
      // antonym|
      // also|
      // participle|
      // pertainym|
      // derivation|
      // domain_topic|
      // has_domain_topic|
      // domain_region|
      // has_domain_region|
      // exemplifies|
      // is_exemplified_by|
      // similar|
      // other|
      simple_aspect_ip|
      secondary_aspect_ip|
      simple_aspect_pi|
      secondary_aspect_pi|
      feminine|
      has_feminine|
      masculine|
      has_masculine|
      young|
      has_young|
      diminutive|
      has_diminutive|
      augmentative|
      has_augmentative|
      anto_gradable|
      anto_simple|
      anto_converse
     */
    }
}
