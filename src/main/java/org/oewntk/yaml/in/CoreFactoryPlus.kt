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
    private val inverses: Boolean = false,
    private val verbose: Boolean = false,
) : CoreProtoFactoryPlus(
    inDir,
    fileext = fileext,
    throws = false,
    verbose = false
), Supplier<CoreModel?> {

    override fun get(): CoreModel? {
        return make()
    }

    private var lexesStubCount = 0
    private var sensesStubCount = 0
    private var synsetsStubCount = 0

    private fun make(): CoreModel? {
        return super.get()?.let { stubModel ->
            lexesStubCount = stubModel.lexes.size
            sensesStubCount = stubModel.senses.size
            synsetsStubCount = stubModel.synsets.size

            if (verbose) Tracing.psInfo.println("[I] Fix")
            return stubModel
                .fix(verbose = verbose)
                .checkMembers(verbose = verbose)
                .apply { if (inverses) generateInverseRelations() }
        }
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
        val lexesDelta = newLexes.size - lexesStubCount
        val sensesDelta = newSenses.size - sensesStubCount
        val synsetsDelta = newSynsets.size - synsetsStubCount
        if (verbose) Tracing.psInfo.println("[I] Fix completed with lexes+=$lexesDelta senses+=$sensesDelta synsets+=$synsetsDelta")
        return CoreModel(newLexes, newSenses, newSynsets)
    }

    companion object {

        /**
         * Make model
         *
         * @param inDir  dir containing release YAML files
         * @return model
         */
        private fun makeModel(inDir: File, verbose: Boolean = false): CoreModel? {
            return CoreFactoryPlus(inDir, verbose = verbose).get()
        }

        /**
         * Make core model from YAML files
         *
         * @param args command-line arguments
         * @return core model
         */
        private fun makeModel(args: Array<String>): CoreModel? {
            var iArg = 0
            var verbose = false
            if (args[iArg] == "--verbose") {
                verbose = true
                iArg++
            }
            val inDir = File(args[iArg])
            return makeModel(inDir, verbose = verbose)
        }

        // C O L L E C T

        private val keyComparator: Comparator<Pair<Lemma, SynsetType>> =
            compareBy<Pair<Lemma, SynsetType>> { it.first }   // Lemma
                .thenBy { it.second }                         // Type

        private fun Collection<Lex>.collectTargetSynsets(senseResolver: (SenseKey) -> Sense) = this
            .flatMap { it.senseKeys }
            .map { senseResolver(it).synsetId }
            .toSet()

        /**
         * Collect orphan members of a synset
         *
         * @param synset synset
         * @return list of (lemma,synsetType) pairs to synsets in which they appear as members but don't have an entry
         */
        fun CoreModel.orphanMembers(synset: Synset): List<Lemma> {
            return synset.members
                .filter {
                    val found = lexFinder(it)
                    found == null
                            || found.none { lex -> lex.partOfSpeech == synset.partOfSpeech }
                            || found.collectTargetSynsets(senseResolver).none { targetIds -> targetIds.contains(synset.synsetId) }
                }
                .toList()
        }

        /**
         * Collect
         *
         * @return list of (lemma,synsetType) pairs to synsets in which they appear as members but don't have an entry
         */
        fun CoreModel.orphanMembers(): Map<Pair<Lemma, SynsetType>, List<Synset>> {
            return synsets
                .map { synset -> synset to orphanMembers(synset) }
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
        private fun CoreModel.generateMemberEntries(verbose: Boolean = false): Pair<List<Lex>, List<Sense>> {
            val orphans = orphanMembers()
            val csv = orphanToCsv(orphans)
            val csvFile = File("plus.log")
            csvFile.writeText(csv)

            Tracing.ps(orphans.isEmpty()).println("[W] ${orphans.size} orphan entries")
            if (verbose) {
                Tracing.psInfo.println("[I] ${orphans.size} orphans logged in $csvFile")
            }
            return generateMemberEntries(orphans, verbose = verbose)
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
        private fun CoreModel.generateMemberEntries(orphans: Map<Pair<Lemma, SynsetType>, List<Synset>>, verbose: Boolean = false): Pair<List<Lex>, List<Sense>> {
            val newLexes = lexes.toMutableList()
            val newSenses = senses.toMutableList()
            orphans.forEach { (typedLemma, synsets) ->
                val (lemma, type) = typedLemma
                val foundLex = Finder.getLexesHavingPos(this, lemma, type.toPartOfSpeech())?.firstOrNull()
                if (foundLex != null) {
                    // lex found with the required part of speech
                    val n = foundLex.senseKeys.size
                    synsets.withIndex().forEach { (idx, synset) ->
                        val resolvedSenses: Sequence<Pair<Sense?, SynsetId?>> = foundLex.senseKeys.asSequence()
                            .map { sk -> senseFinder(sk) }
                            .map { sense -> sense to sense?.let { synsetFinder(sense.synsetId)?.synsetId } }
                        val found: Pair<Sense?, SynsetId?>? = resolvedSenses.firstOrNull { synset.synsetId == it.second }
                        if (found?.first == null) {
                            // no sense found with the required synset target: add generated sense to the found lex
                            val senseId = generateSenseKey(lemma, synset, idx)
                            val sense = Sense(senseId, foundLex.key, synset.synsetId, indexInLex = n + idx) // TODO compute indexInLex (idx is the index of the synset in the orphan entry)
                            foundLex.senseKeys += senseId  // TODO sort by sense order
                            newSenses.add(sense)
                        }
                    }
                } else {
                    // no lex found with the required part of speech: create one
                    val lex = Lex(lemma, type.value.toString(), generated = true) //, source = findFile(lemma, generated = generated))
                    lex.senseKeys = synsets.withIndex().map { (idx, synset) ->
                        val senseId = generateSenseKey(lemma, synset, idx)
                        val sense = Sense(senseId, lex.key, synset.synsetId, indexInLex = idx) // TODO compute indexInLex (idx is the index of the synset in the orphan entry)
                        newSenses.add(sense)
                        senseId
                    }.toList()
                    newLexes.add(lex)
                }
            }
            if (verbose) Tracing.psInfo.println("[I] -orphan members and senses added")
            return newLexes.sorted() to newSenses.sorted()
        }

        fun CoreModel.generateSynsets(verbose: Boolean = false): List<Synset> {
            if (verbose) Tracing.psInfo.println("[I] -no synset added")
            return synsets
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