/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.oewntk.model.*
import org.oewntk.model.PartOfSpeech
import org.oewntk.model.Relation.Companion.SYNSET_RELATIONS
import org.oewntk.model.Synset
import org.oewntk.model.Synset.Companion.VALID_SYNSET_RELATIONS
import org.oewntk.model.SynsetType
import org.oewntk.model.distinctOrDo
import org.oewntk.yaml.`in`.YamlUtils.assertKeysIn
import org.oewntk.yaml.`in`.YamlUtils.processExamples
import org.oewntk.yaml.`in`.YamlUtils.safeCast
import org.oewntk.yaml.`in`.YamlUtils.safeNullableCast
import java.io.File

/**
 * Synset YAML parser
 *
 * @param dir dir containing YAML files
 */
class SynsetParser(
    dir: File,
    val fileext: String = "yaml",
    verbose: Boolean = false,
    throws: Boolean = true,
) : YamlProcessor1<Synset, String, Map<String, *>>(dir, throws = throws, verbose = verbose) {

    override val files: Array<File>
        get() = dir.listFiles { f: File -> f.name.matches("(${PartOfSpeech.N.fullName}|${PartOfSpeech.V.fullName}|${PartOfSpeech.A.fullName}|${PartOfSpeech.R.fullName}).*\\.$fileext".toRegex()) }!!

    override fun processEntry(source: String?, entry: Pair<String, Map<String, *>>): Synset {
        val id = entry.first
        val synsetMap = entry.second
        if (DUMP) {
            Tracing.psInfo.println(id)
            dumpMap(synsetMap)
        }
        assertKeysIn(
            throws = throws,
            synsetMap.keys,
            VALID_SYNSET_RELATIONS,
            KEY_SYNSET_POS,
            KEY_SYNSET_MEMBERS,
            KEY_SYNSET_DEFINITION,
            KEY_SYNSET_EXAMPLE,
            KEY_SYNSET_USAGE,
            KEY_SYNSET_WIKIDATA,
            KEY_SYNSET_ILI,
            KEY_SYNSET_SOURCE,
            KEY_SYNSET_DOMAIN,
        )

        val code = synsetMap[KEY_SYNSET_POS] as String?
        val definitions = safeCast<List<String>>(synsetMap[KEY_SYNSET_DEFINITION]!!)
        val members = safeCast<List<String>>(synsetMap[KEY_SYNSET_MEMBERS]!!)
        val examples = processExamples(safeNullableCast<List<String>>(synsetMap[KEY_SYNSET_EXAMPLE]), KEY_EXAMPLE_TEXT, KEY_EXAMPLE_SOURCE, throws = throws)
        val usages = safeNullableCast<List<String>>(synsetMap[KEY_SYNSET_USAGE])
        val ili = safeNullableCast<String>(synsetMap[KEY_SYNSET_ILI])
        val wikidata = synsetMap[KEY_SYNSET_WIKIDATA]?.let {
            when (it) {
                is String -> listOf(it)
                is List<*> -> safeCast<List<String>>(it).ifEmpty { null }
                else -> null as List<String>?
            }
        }
        val domain = safeNullableCast<String>(synsetMap[KEY_SYNSET_DOMAIN]) ?: source!!.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
        val source: String? = safeNullableCast(synsetMap[KEY_SYNSET_SOURCE])

        // members
        val synsetMembers = if (DISTINCT_MEMBERS)
            members
                .distinctOrDo { duplicate -> if (WARN_DUPLICATE_MEMBERS) Tracing.psErr.println("[E] duplicate $duplicate in members $members of $id") }
                .map(::Lemma)
        else members
            .map(::Lemma)

        // relations
        val relations = if (IGNORE_QTARGETS)
            SYNSET_RELATIONS
                .asSequence()
                .filter { relation -> synsetMap.containsKey(relation) }
                .map { relation ->
                    Relation(relation) to safeCast<List<String>>(synsetMap[relation]!!)
                        .filter { targetId -> targetId[0] != 'Q' }
                        .map { SynsetId(it) }
                        .toSet() // relation, setOf(targets)
                }
                .filter { (_, targetIds) -> targetIds.isNotEmpty() }
                .toMap()
                .ifEmpty { null }
        else
            SYNSET_RELATIONS
                .asSequence()
                .filter { rel -> synsetMap.containsKey(rel) }
                .map { rel -> Relation(rel) }
                .associateWith { relation -> safeCast<List<String>>(synsetMap[relation.id]!!).map { SynsetId(it) }.toSet() } // relation, setOf(targets)
                .ifEmpty { null }

        // type
        val type = code!![0]

        return Synset(
            SynsetId(id),
            SynsetType.fromChar(type),
            domain,
            synsetMembers.toSet(),
            definitions,
            examples,
            usages,
            relations,
            ili,
            wikidata
        ).apply {
            this.source = source
        }
    }

    companion object {

        private const val IGNORE_QTARGETS = true

        private const val WARN_DUPLICATE_MEMBERS = true

        private const val DISTINCT_MEMBERS = true

        private const val DUMP = false

        private const val KEY_SYNSET_POS = "partOfSpeech"
        private const val KEY_SYNSET_DEFINITION = "definition"
        private const val KEY_SYNSET_MEMBERS = "members"
        private const val KEY_SYNSET_EXAMPLE = "example"
        private const val KEY_SYNSET_USAGE = "usage"
        private const val KEY_SYNSET_ILI = "ili"
        private const val KEY_SYNSET_WIKIDATA = "wikidata"
        private const val KEY_SYNSET_SOURCE = "source"
        private const val KEY_SYNSET_DOMAIN = "domain"

        private const val KEY_EXAMPLE_SOURCE = "source"
        private const val KEY_EXAMPLE_TEXT = "text"
    }
}
