/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.oewntk.model.*
import org.oewntk.model.SenseKeys.escapeForSenseKey
import org.oewntk.model.SenseKeys.toLexFileNum
import org.oewntk.model.SenseKeys.toPosNum
import org.oewntk.model.SenseKeys.toPosTag
import java.io.File
import java.util.function.Supplier

/**
 * Model factory
 *
 * @param inDir  dir containing release YAML files
 * @param inDir2 dir containing extra YAML files
 */
class FactoryPlus(private val inDir: File, private val inDir2: File) : Supplier<Model?> {

    override fun get(): Model? {
        return readModelAndFix(inDir, inDir2)
    }

    private fun readModelAndFix(inDir: File, inDir2: File): Model? {
        val stubModel: Model? = Factory(inDir, inDir2).get()
        return stubModel?.let { model ->
            Tracing.psInfo.printf("[Model] %s%n%s%n%s%n", model.sources.contentToString(), model.info(), ModelInfo.counts(stubModel))
            // model.check()
            Tracing.psInfo.println("Check synset relation targets")
            model.checkSynsetRelationTargets()
            Tracing.psInfo.println("Check sense relation targets")
            model.checkSenseRelationTargets()
            Tracing.psInfo.println("Check members")
            model.checkMembers(verbose = VERBOSE)

            return model
                .fixMemberEntries()
                .checkMembers(verbose = VERBOSE)
        }
    }

    companion object {

        val VERBOSE = false

        /**
         * Make model
         *
         * @param inDir  dir containing release YAML files
         * @param inDir2 dir containing extra YAML files
         * @return model
         */
        private fun makeModel(inDir: File, inDir2: File): Model? {
            return FactoryPlus(inDir, inDir2).get()
        }

        /**
         * Make core model from YAML files
         *
         * @param args command-line arguments
         * @return core model
         */
        fun makeModel(args: Array<String>): Model? {
            val inDir = File(args[0])
            val inDir2 = File(args[1])
            return makeModel(inDir, inDir2)
        }

        // C O L L E C T

        /**
         * Collect
         *
         * @return list of (lemma,pos) pairs to synsets in which they appear as members but don't have an entry
         */
        fun CoreModel.orphanMembers(): Map<Pair<Lemma, Char>, List<Synset>> {
            return synsets
                .map { synset -> synset to synset.members.filter { lexesByLemma!![it] == null || lexesByLemma!![it]?.none { lex -> lex.type == synset.type } ?: true }.toList() }
                .filter { (_, lemmas) -> lemmas.isNotEmpty() }
                .flatMap { (synset, lemmas) -> lemmas.map { lemma -> synset to (lemma to synset.type) } }
                .groupBy({ it.second }, { it.first })
        }

        // F I X

        /**
         * Fix this model
         *
         * @return a new fixed model
         */
        fun Model.fixMemberEntries(): Model {
            val orphans = orphanMembers()
            if (VERBOSE) {
                orphans.forEach { (lemma, synsets) ->
                    Tracing.psErr.println("[E] lemma $lemma member of  {${synsets.joinToString()}}")
                }
            }
            Tracing.psErr.println("[I] ${orphans.size} orphan entries")
            return fixMemberEntries(orphans)
        }

        /**
         * Fix
         *
         * @param pseudos list of (lemma,pos) pairs to synsets in which they appear as members but don't have an entry
         * @return a new fixed model
         */
        fun Model.fixMemberEntries(pseudos: Map<Pair<Lemma, Category>, List<Synset>>): Model {
            val newLexes = lexes.toMutableList()
            val newSenses = senses.toMutableList()
            pseudos.forEach { (typedLemma, synsets) ->
                val (lemma, type) = typedLemma
                val lex = Lex(lemma, type.toString(), source = "entries-pseudo.yaml")
                lex.senseKeys = synsets.withIndex().map { (idx, synset) ->
                    val ssType = synset.type.toPosNum()
                    val escapedLemma = lemma.escapeForSenseKey()
                    val lexfileNum = "%02d".format(synset.lexfile!!.toLexFileNum())
                    val lexfileIdx = "%02d".format(idx + 1)
                    val senseid = "$escapedLemma%$ssType:$lexfileNum:$lexfileIdx::"
                    val sense = Sense(senseid, lex, synset.type, 0, synset.synsetId)
                    newSenses.add(sense)
                    senseid
                }.toList()
                newLexes.add(lex)
            }
            return Model(newLexes, newSenses, synsets, verbFrames, verbTemplates)
        }

        /**
         * Main
         *
         * @param args command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val model = makeModel(args)
        }
    }
}