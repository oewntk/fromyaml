/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.oewntk.model.Synset
import org.oewntk.yaml.`in`.YamlUtils.assertKeysIn
import org.oewntk.yaml.`in`.YamlUtils.safeCast
import org.oewntk.yaml.`in`.YamlUtils.safeNullableCast
import org.yaml.snakeyaml.error.YAMLException
import java.io.File
import java.util.*

/**
 * Synset YAML parser
 *
 * @param dir dir containing YAML files
 */
class SynsetParser(dir: File) : YamProcessor1<Synset, String, Map<String, *>>(dir) {

    override val files: Array<File>
        get() = dir.listFiles { f: File -> f.name.matches("(noun|verb|adj|adv).*\\.yaml".toRegex()) }!!

    override fun processEntry(source: String?, entry: Pair<String, Map<String, *>>): Synset {
        val domain = source!!.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
        val id = entry.first
        val synsetMap = entry.second
        if (DUMP) {
            Tracing.psInfo.println(id)
            dumpMap(synsetMap)
        }
        assertKeysIn(
            synsetMap.keys,
            VALID_SYNSET_RELATIONS,
            KEY_SYNSET_POS,
            KEY_SYNSET_DEFINITION,
            KEY_SYNSET_EXAMPLE,
            KEY_SYNSET_MEMBERS,
            KEY_SYNSET_WIKIDATA,
            KEY_SYNSET_ILI,
            KEY_SYNSET_SOURCE
        )

        val code = synsetMap[KEY_SYNSET_POS] as String?
        val definitions: List<String> = safeCast(synsetMap[KEY_SYNSET_DEFINITION]!!)
        val examples = synsetMap[KEY_SYNSET_EXAMPLE] as List<*>?
        val members: List<String> = safeCast(synsetMap[KEY_SYNSET_MEMBERS]!!)
        val wikidata: String? = safeNullableCast(synsetMap[KEY_SYNSET_WIKIDATA])

        // provision for no duplicates in members
        assert(members.none { Collections.frequency(members, it) > 1 })

        // relations
        val relations = SYNSET_RELATIONS
            .asSequence()
            .filter { relation -> synsetMap.containsKey(relation) }
            .map { relation -> relation to safeCast<List<String>>(synsetMap[relation]!!).toMutableSet() } // relation, setOf(targets)
            .toMap()
            .toMutableMap()
            .ifEmpty { null }

        // type
        val type = code!![0]

        return Synset(id, type, domain, members.toTypedArray(), definitions.toTypedArray(), examples?.examplesToArray(), relations, wikidata)
    }

    companion object {

        private const val DUMP = false

        private const val KEY_SYNSET_POS = "partOfSpeech"
        private const val KEY_SYNSET_DEFINITION = "definition"
        private const val KEY_SYNSET_EXAMPLE = "example"
        private const val KEY_SYNSET_MEMBERS = "members"
        private const val KEY_SYNSET_ILI = "ili"
        private const val KEY_SYNSET_WIKIDATA = "wikidata"
        private const val KEY_SYNSET_SOURCE = "source"

        private const val KEY_EXAMPLE_SOURCE = "source"
        private const val KEY_EXAMPLE_TEXT = "text"

        private val VALID_SYNSET_RELATIONS = arrayOf(

            "hypernym",  // "hyponym",
            "instance_hypernym",  // "instance_hyponym",
            "mero_part",  // "holo_part",
            "mero_member",  // "holo_member",
            "mero_substance",  // "holo_substance",
            "causes",  // "is_caused_by",
            "entails",  // "is_entailed_by",
            "exemplifies",  // "is_exemplified_by",
            "domain_topic",  // "has_domain_topic"
            "domain_region",  // "has_domain_region"
            "attribute",
            "similar",
            "also",
        )

        private val SYNSET_RELATIONS = arrayOf(

            "hypernym", "hyponym",
            "instance_hypernym", "instance_hyponym",
            "mero_part", "holo_part",
            "mero_member", "holo_member",
            "mero_substance", "holo_substance",
            "causes", "is_caused_by",
            "entails", "is_entailed_by",
            "exemplifies", "is_exemplified_by",
            "domain_topic", "has_domain_topic",
            "domain_region", "has_domain_region",
            "attribute",
            "similar",
            "also",
        )

        /*
    ignored
      agent|
      // also|
      // attribute|
      be_in_state|
      // causes|
      classified_by|
      classifies|
      co_agent_instrument|
      co_agent_patient|
      co_agent_result|
      co_instrument_agent|
      co_instrument_patient|
      co_instrument_result|
      co_patient_agent|
      co_patient_instrument|
      co_result_agent|
      co_result_instrument|
      co_role|
      direction|
      // domain_region|
      // domain_topic|
      // exemplifies|
      // entails|
      eq_synonym|
      // has_domain_region|
      // has_domain_topic|
      // is_exemplified_by|
      holo_location|
      // holo_member|
      // holo_part|
      holo_portion|
      // holo_substance|
      holonym|
      // hypernym|
      // hyponym|
      in_manner|
      // instance_hypernym|
      // instance_hyponym|
      instrument|
      involved|
      involved_agent|
      involved_direction|
      involved_instrument|
      involved_location|
      involved_patient|
      involved_result|
      involved_source_direction|
      involved_target_direction|
      // is_caused_by|
      // is_entailed_by|
      location|
      manner_of|
      mero_location|
      // mero_member|
      // mero_part|
      mero_portion|
      // mero_substance|
      meronym|
      // similar|
      // other|
      patient|
      restricted_by|
      restricts|
      result|
      role|
      source_direction|
      state_of|
      target_direction|
      subevent|
      is_subevent_of|
      // antonym|
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
      anto_converse|
      ir_synonym
     */

        /**
         * Examples to array
         *
         * @receiver examples examples
         * @return array of examples
         */
        fun List<*>.examplesToArray(): Array<String> {
            return Array(this.size) {
                when (this[it]) {
                    is String    -> {
                        val example = this[it] as String
                        processExample(example)
                    }

                    is Map<*, *> -> {
                        val exampleMap: Map<String, *> = safeCast(this[it]!!)
                        assertKeysIn(exampleMap.keys, KEY_EXAMPLE_SOURCE, KEY_EXAMPLE_TEXT)
                        val example = exampleMap[KEY_EXAMPLE_TEXT].toString()
                        processExample(example)
                    }

                    else         -> throw YAMLException(this[it].toString())
                }
            }
        }

        private const val EXAMPLES_FIX_QUOTES = true

        // TODO trimming examples
        private const val LEGACY_V1 = true

        private const val EXAMPLES_TRIM = !LEGACY_V1

        fun processExample(example: String): String {
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

        private fun countQuotes(str: String): Int {
            var quoteCount = 0
            var p = -1
            while ((str.indexOf('"', p + 1).also { p = it }) != -1) {
                quoteCount++
            }
            return quoteCount
        }
    }
}
