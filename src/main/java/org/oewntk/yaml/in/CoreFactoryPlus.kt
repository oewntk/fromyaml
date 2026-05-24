/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.oewntk.model.*
import org.oewntk.model.SenseKeys.escapeForSenseKey
import org.oewntk.model.SenseKeys.toLexFileNum
import org.oewntk.model.SenseKeys.toPosNum
import java.io.File
import java.util.function.Supplier

/**
 * Model factory
 *
 * @param inDir  dir containing release YAML files
 */
class CoreFactoryPlus(private val inDir: File, val fileext: String = "yaml", val verbose: Boolean = false) : Supplier<CoreModel?> {

    override fun get(): CoreModel? {
        return make(inDir)
    }

    private fun make(inDir: File): CoreModel? {
        val stubModel: CoreModel? = CoreFactory(inDir, fileext = fileext, verbose = verbose).get()
        return stubModel?.let { model ->
            if (verbose) Tracing.psInfo.printf("[Model] %s%n%s%n%s%n", model.source, model.info(), ModelInfo.counts(stubModel))
            model.check(verbose = verbose)
            if (verbose) Tracing.psInfo.printf("[I] Fixing ...")
            return model
                .fix(verbose = verbose)
                .checkMembers(verbose = verbose)
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
                            found == null || found.none { lex -> lex.type == synset.type }
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
            if (verbose) {
                //orphans.forEach { (lemma, synsets) ->
                //    Tracing.psErr.println("[E] lemma $lemma member of  {${synsets.joinToString()}}")
                //}
                val csv = orphans
                    .map { (key, synsets) -> "${key.first};${key.second};${synsets.joinToString(separator = ",") { it.synsetId }}" }
                    .joinToString(separator = "\n")
                Tracing.psInfo.println("[W] orphans ${orphans.size}\n$csv")
            }
            Tracing.psErr.println("[I] ${orphans.size} orphan entries")
            return generateMemberEntries(orphans)
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
                val lex = Lex(lemma, type.value.toString(), generated = true) //, source = findFile(lemma, generated = generated))
                lex.senseKeys = synsets.withIndex().map { (idx, synset) ->
                    val ssType = synset.type.toPosNum()
                    val escapedLemma = lemma.escapeForSenseKey()
                    val lexfileNum = "%02d".format(synset.lexfile.toLexFileNum())
                    val lexfileIdx = "%02d".format(idx + 1)
                    val senseid = "$escapedLemma%$ssType:$lexfileNum:$lexfileIdx::"
                    val sense = Sense(senseid, lex, synset.type, 0, synset.synsetId)
                    newSenses.add(sense)
                    senseid
                }.toList()
                newLexes.add(lex)
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