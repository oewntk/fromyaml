/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.oewntk.model.*
import org.oewntk.model.Sense.Companion.SENSE_RELATIONS
import org.oewntk.model.Sense.Companion.VALID_SENSE_RELATIONS
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
class LexParser(
    dir: File,
    val fileext: String = "yaml",
    verbose: Boolean = false,
    val throws: Boolean = true,
) : YamlProcessor<Lex, String, Map<String, *>>(dir, verbose = verbose) {

    /**
     * Accumulated senses as lexes are processed
     */
    val senses: MutableList<Sense> = ArrayList()

    override val files: Array<File>
        get() = dir.listFiles { f: File -> f.name.matches("entries.*\\.$fileext".toRegex()) }!!

    override fun processEntry(source: String?, entry: Pair<String, Map<String, *>>): Collection<Lex> {
        try {
            val lexes: MutableList<Lex> = ArrayList()

            val lemma = entry.first
            val lemmaMap = entry.second
            for ((k2, value1) in lemmaMap) {
                val type = SynsetType.fromChar(k2[0])
                val lexMap: Map<String, *> = safeCast(value1!!)
                assertKeysIn(
                    throws = throws,
                    lexMap.keys,
                    KEY_LEX_SENSE,
                    KEY_LEX_PRONUNCIATION,
                    KEY_LEX_FORM,
                )
                if (DUMP) {
                    dumpMap(lexMap)
                }

                // lex
                val lex = Lex(lemma, k2)

                // senses
                val senseMaps: List<Map<String, *>> = safeCast(lexMap[KEY_LEX_SENSE]!!)
                val lexSenses = MutableList(senseMaps.size) {
                    val senseMap = senseMaps[it]
                    assertKeysIn(
                        throws = throws,
                        senseMap.keys,
                        VALID_SENSE_RELATIONS,
                        KEY_SENSE_ID,
                        KEY_SENSE_SYNSET,
                        KEY_SENSE_VERBFRAMES,
                        KEY_SENSE_VERBTEMPLATES,
                        KEY_SENSE_ADJPOSITION,
                        KEY_SENSE_TAGCOUNT,
                        KEY_SENSE_EXAMPLES,
                    )
                    if (DUMP) {
                        dumpMap(senseMap, indent = "\t")
                    }

                    val senseId = senseMap[KEY_SENSE_ID]!! as SenseKey
                    val synsetId = senseMap[KEY_SENSE_SYNSET]!! as SynsetId
                    val examples: List<Pair<String, String?>>? = processExamples(
                        safeNullableCast(senseMap[KEY_SENSE_EXAMPLES]),
                        KEY_EXAMPLE_TEXT,
                        KEY_EXAMPLE_SOURCE
                    )
                    val verbFrames: List<VerbFrameId>? = safeNullableCast(senseMap[KEY_SENSE_VERBFRAMES])
                    val verbTemplates: List<VerbTemplateId>? = safeNullableCast(senseMap[KEY_SENSE_VERBTEMPLATES])
                    val adjPosition = senseMap[KEY_SENSE_ADJPOSITION] as String?
                    val tagCount = senseMap[KEY_SENSE_TAGCOUNT] as Int?

                    // relations
                    val relations = if (IGNORE_QTARGETS)
                        SENSE_RELATIONS
                            .asSequence()
                            .filter { relation -> senseMap.containsKey(relation) }
                            .associateWith { relation ->
                                safeCast<List<String>>(senseMap[relation]!!).toSet()
                            }
                            .ifEmpty { null }
                    else
                        SENSE_RELATIONS
                            .asSequence()
                            .filter { relation -> senseMap.containsKey(relation) }
                            .map { relation ->
                                relation to safeCast<List<String>>(senseMap[relation]!!).filter { target -> target[0] != 'Q' }
                                    .toSet()
                            }
                            .filter { (_, targets) -> targets.isNotEmpty() }
                            .toMap()
                            .ifEmpty { null }

                    // sense
                    val lexSense = Sense(
                        senseId,
                        lex.key,
                        synsetId,
                        indexInLex = it,
                        examples?.toTypedArray(),
                        verbFrames?.toTypedArray(),
                        verbTemplates?.toTypedArray(),
                        adjPosition,
                        tagCount,
                        relations
                    )
                    senses.add(lexSense)
                    lexSense
                }
                lex.senseKeys = lex.senseKeys.toMutableList() + lexSenses.map { it.senseKey }.asSequence()

                // pronunciations
                val pronunciationList: List<Map<String, *>>? = safeNullableCast(lexMap[KEY_LEX_PRONUNCIATION])
                val pronunciations: Array<Pronunciation>? = if (pronunciationList != null) {
                    Array(pronunciationList.size) {
                        val pronunciationMap = pronunciationList[it]
                        assertKeysIn(
                            throws = throws,
                            pronunciationMap.keys,
                            KEY_PRONUNCIATION_VARIETY,
                            KEY_PRONUNCIATION_VALUE
                        )
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

        private const val IGNORE_QTARGETS = true

        private const val DUMP = false

        private const val KEY_LEX_SENSE = "sense"
        private const val KEY_LEX_PRONUNCIATION = "pronunciation"
        private const val KEY_LEX_FORM = "form"

        private const val KEY_SENSE_ID = "id"
        private const val KEY_SENSE_SYNSET = "synset"
        private const val KEY_SENSE_ADJPOSITION = "adjposition"
        private const val KEY_SENSE_VERBFRAMES = "subcat"
        private const val KEY_SENSE_EXAMPLES = "sent"
        private const val KEY_SENSE_VERBTEMPLATES = "template" // not compat
        private const val KEY_SENSE_TAGCOUNT = "tagcount" // not compat

        private const val KEY_EXAMPLE_SOURCE = "source"
        private const val KEY_EXAMPLE_TEXT = "text"

        private const val KEY_PRONUNCIATION_VARIETY = "variety"
        private const val KEY_PRONUNCIATION_VALUE = "value"
    }
}
