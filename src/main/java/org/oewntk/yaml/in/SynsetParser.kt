/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.oewntk.model.PartOfSpeech
import org.oewntk.model.Synset
import org.oewntk.model.Synset.Companion.SYNSET_RELATIONS
import org.oewntk.model.Synset.Companion.VALID_SYNSET_RELATIONS
import org.oewntk.model.SynsetType
import org.oewntk.yaml.`in`.YamlUtils.assertKeysIn
import org.oewntk.yaml.`in`.YamlUtils.processExamples
import org.oewntk.yaml.`in`.YamlUtils.safeCast
import org.oewntk.yaml.`in`.YamlUtils.safeNullableCast
import java.io.File
import java.util.*

/**
 * Synset YAML parser
 *
 * @param dir dir containing YAML files
 */
class SynsetParser(
    dir: File,
    val fileext: String = "yaml",
    verbose: Boolean = false,
    val throws: Boolean = true,
) : YamlProcessor1<Synset, String, Map<String, *>>(dir, verbose = verbose) {

    override val files: Array<File>
        get() = dir.listFiles { f: File -> f.name.matches("(${PartOfSpeech.N.fullName}|${PartOfSpeech.V.fullName}|${PartOfSpeech.A.fullName}|${PartOfSpeech.R.fullName}).*\\.$fileext".toRegex()) }!!

    override fun processEntry(source: String?, entry: Pair<String, Map<String, *>>): Synset {
        val domain = source!!.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
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
        )

        val code = synsetMap[KEY_SYNSET_POS] as String?
        val definitions: List<String> = safeCast(synsetMap[KEY_SYNSET_DEFINITION]!!)
        val members: List<String> = safeCast(synsetMap[KEY_SYNSET_MEMBERS]!!)
        val examples: List<Pair<String, String?>>? =
            processExamples(safeNullableCast(synsetMap[KEY_SYNSET_EXAMPLE]), KEY_EXAMPLE_TEXT, KEY_EXAMPLE_SOURCE)
        val usages: List<String>? = safeNullableCast(synsetMap[KEY_SYNSET_USAGE])
        val ili: String? = safeNullableCast(synsetMap[KEY_SYNSET_ILI])
        val wikidatas = synsetMap[KEY_SYNSET_WIKIDATA]
        val wikidata: List<String>? = when {
            wikidatas is String -> listOf(wikidatas)
            wikidatas is List<*> && wikidatas.isNotEmpty() -> {
                @Suppress("UNCHECKED_CAST")
                wikidatas as List<String>?
            }

            else -> null as List<String>?
        }
        val source: String? = safeNullableCast(synsetMap[KEY_SYNSET_SOURCE])

        // members
        if (WARN_DUPLICATE_MEMBERS) {
            val noDuplicates = members.none { Collections.frequency(members, it) > 1 }
            //assert(noDuplicates) { Tracing.psErr.println("[E] duplicate members in $id: $members") }
            if (!noDuplicates) {
                Tracing.psErr.println("[E] duplicate members in $id: $members")
            }
        }
        val synsetMembers = if (DISTINCT_MEMBERS) members.distinct() else members

        // relations
        val relations = if (IGNORE_QTARGETS)
            SYNSET_RELATIONS
                .asSequence()
                .filter { relation -> synsetMap.containsKey(relation) }
                .map { relation ->
                    relation to safeCast<List<String>>(synsetMap[relation]!!).filter { target -> target[0] != 'Q' }
                        .toSet() // relation, setOf(targets)
                }
                .filter { (_, targets) -> targets.isNotEmpty() }
                .toMap()
                .ifEmpty { null }
        else
            SYNSET_RELATIONS
                .asSequence()
                .filter { relation -> synsetMap.containsKey(relation) }
                .associateWith { relation -> safeCast<List<String>>(synsetMap[relation]!!).toSet() } // relation, setOf(targets)
                .ifEmpty { null }

        // type
        val type = code!![0]

        return Synset(
            id,
            SynsetType.fromChar(type),
            domain,
            synsetMembers.toTypedArray(),
            definitions.toTypedArray(),
            examples?.toTypedArray(),
            usages?.toTypedArray(),
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

        private const val KEY_EXAMPLE_SOURCE = "source"
        private const val KEY_EXAMPLE_TEXT = "text"
    }
}
