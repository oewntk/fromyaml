/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.oewntk.model.*
import org.oewntk.model.SenseKeys.generateSenseKey
import org.oewntk.model.Validator.check
import org.oewntk.model.Validator.checkMembers
import java.io.File
import java.util.function.Supplier

/**
 * Model factory
 *
 * @param inDir  dir containing release YAML files
 */
class CoreFactoryPlus(
    inDir: File,
    fileext: String = "yaml",
    val verbose: Boolean = false
) : CoreProtoFactoryPlus(inDir, fileext = fileext, verbose = false), Supplier<CoreModel?> {

    override fun get(): CoreModel? {
        return make()
    }

    private fun make(): CoreModel? {
        return super.get()?.let { stubModel ->
            if (verbose) Tracing.psInfo.println("[I] Fixing ...")
            return stubModel
                .fix(verbose = false)
                .checkMembers(verbose = true)
        }
    }

    companion object {

        /**
         * Make model
         *
         * @param inDir  dir containing release YAML files
         * @return model
         */
        private fun makeModel(inDir: File): CoreModel? {
            return CoreFactoryPlus(inDir).get()
        }

        /**
         * Make core model from YAML files
         *
         * @param args command-line arguments
         * @return core model
         */
        fun makeModel(args: Array<String>): CoreModel? {
            val inDir = File(args[0])
            return makeModel(inDir)
        }

        // C O L L E C T

        private val keyComparator: Comparator<Pair<Lemma, SynsetType>> =
            compareBy<Pair<Lemma, SynsetType>> { it.first }   // Lemma
                .thenBy { it.second }                   // Category

        private fun Collection<Lex>.collectTargetSynsets(senseResolver: (SenseKey) -> Sense) = this
            .flatMap { it.senseKeys }
            .map { senseResolver(it).synsetId }
            .toSet()

        fun CoreModel.orphans(synset: Synset) = synset.members
            .filter {
                val found = lexFinder(it)
                found == null
                        || found.none { lex -> lex.type == synset.type }
                        || found.collectTargetSynsets(senseResolver).none { targets -> targets.contains(synset.synsetId) }
            }
            .toList()

        // private fun Collection<Lex>.collectTargetSynsets(senseResolver: (SenseKey) -> Sense) = this
        //     .flatMap { it.senseKeys }
        //     .map { senseResolver(it).synsetId }
        //     .toSet()

        // fun Synset.orphans(senseResolver: (SenseKey) -> Sense, lexFinder: (Lemma) -> Collection<Lex>?) = this.members
        //     .filter {
        //         val found = lexFinder(it)
        //         found == null
        //                 || found.none { lex -> lex.type == this.type }
        //                 || found.collectTargetSynsets(senseResolver).none { targets -> targets.contains(this.synsetId) }
        //     }
        //     .toList()

        /**
         * Collect
         *
         * @return list of (lemma,synsetType) pairs to synsets in which they appear as members but don't have an entry
         */
        private fun CoreModel.orphanMembers(): Map<Pair<Lemma, SynsetType>, List<Synset>> {
            return synsets
                .map { synset ->
                    synset to synset.members
                        .filter {
                            val found = lexFinder(it)
                            found == null
                                    || found.none { lex -> lex.type == synset.type }
                                    || found.collectTargetSynsets(senseResolver).none { targets -> targets.contains(synset.synsetId) }
                        }
                        .toList()
                }
                .filter { (_, lemmas) -> lemmas.isNotEmpty() }
                .flatMap { (synset, lemmas) -> lemmas.map { lemma -> synset to (lemma to synset.type) } }
                .groupBy({ it.second }, { it.first })
                .toSortedMap(keyComparator)
        }

        // F I X

        /**
         * Generate new lexes and new senses this model
         *
         * @return new list of lexes and new list of senses
         */
        private fun CoreModel.generateMemberEntries(verbose: Boolean = false): Pair<Collection<Lex>, Collection<Sense>> {
            val orphans = orphanMembers()
            val csv = orphanToCsv(orphans)
            val csvFile = File("plus.log")
            csvFile.writeText(csv)

            if (verbose) {
                Tracing.psInfo.println("[W] ${orphans.size} fixes logged in $csvFile")
            }

            Tracing.ps(orphans.isEmpty()).println("[I] ${orphans.size} orphan entries")
            return generateMemberEntries(orphans)
        }

        private fun orphanToCsv(orphans: Map<Pair<Lemma, SynsetType>, List<Synset>>): String {
            return orphans
                .map { (key, synsets) ->
                    val synsetIds = synsets.joinToString(separator = ",") { it.synsetId }
                    val senseKeys = synsets.withIndex().joinToString(separator = ",") { (idx, synset) -> generateSenseKey(key.first, synset, idx) }
                    "${key.first};${key.second.value};$synsetIds;$senseKeys"
                }
                .sortedWith(LexicographicOrder.lowerFirst)
                .joinToString(separator = "\n")
        }

        /**
         * Generate new lexes and new senses this model
         *
         * @param orphans list of (lemma,pos) pairs to synsets in which they appear as members but don't have an entry
         * @return new list of lexes and new list of senses
         */
        private fun CoreModel.generateMemberEntries(orphans: Map<Pair<Lemma, SynsetType>, List<Synset>>): Pair<Collection<Lex>, Collection<Sense>> {
            val newLexes = lexes.toMutableList()
            val newSenses = senses.toMutableList()
            orphans.forEach { (typedLemma, synsets) ->
                val (lemma, type) = typedLemma
                val foundLex = Finder.getLexesHavingType(this, lemma, type)?.firstOrNull()
                if (foundLex != null) {
                    synsets.withIndex().forEach { (idx, synset) ->
                        val resolvedSenses: Sequence<Pair<Sense?, SynsetId?>> = foundLex.senseKeys.asSequence()
                            .map { sk -> senseFinder(sk) }
                            .map { sense -> sense to sense?.let { synsetFinder(sense.synsetId)?.synsetId } }
                        val found: Pair<Sense?, SynsetId?>? = resolvedSenses.firstOrNull { synset.synsetId == it.second }
                        if (found == null || found.first == null) {
                            val senseId = generateSenseKey(lemma, synset, idx)
                            val sense = Sense(senseId, foundLex.key, synset.synsetId, synset.type, indexInLex = idx) // TODO compute indexInLex (idx is the index of the synset in the orphan entry)
                            foundLex.senseKeys = foundLex.senseKeys + senseId  // TODO sort by sense order
                            newSenses.add(sense)
                        }
                    }

                } else {
                    val lex = foundLex ?: Lex(lemma, type.value.toString(), generated = true) //, source = findFile(lemma, generated = generated))
                    lex.senseKeys = synsets.withIndex().map { (idx, synset) ->
                        val senseId = generateSenseKey(lemma, synset, idx)
                        val sense = Sense(senseId, lex.key, synset.synsetId, synset.type, indexInLex = idx) // TODO compute indexInLex (idx is the index of the synset in the orphan entry)
                        newSenses.add(sense)
                        senseId
                    }.toList()
                    newLexes.add(lex)
                }
            }
            return newLexes to newSenses
        }

        fun CoreModel.generateSynsets(verbose: Boolean = false): Collection<Synset> {
            if (verbose) Tracing.psErr.println("[I] synsets as read from plus")
            return synsets
        }

        /**
         * Fix
         *
         * @receiver model
         * @return a new fixed model
         */
        fun CoreModel.fix(verbose: Boolean = false): CoreModel {
            val (newLexes, newSenses) = generateMemberEntries(verbose = verbose)
            val newSynsets = generateSynsets(verbose = verbose)
            if (verbose) Tracing.psInfo.println("[I] plus completed")
            return CoreModel(newLexes, newSenses, newSynsets)
        }

        /**
         * Main
         *
         * @param args command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val model = makeModel(args)
            if (model?.check(verbose = true) == null) {
                Tracing.psErr.println("null model")
            }
        }
    }
}