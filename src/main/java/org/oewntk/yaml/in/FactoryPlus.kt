/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.oewntk.model.*
import java.io.File
import java.util.function.Supplier

/**
 * Model factory
 *
 * @param inDir  dir containing release YAML files
 * @param inDir2 dir containing extra YAML files
 */
class FactoryPlus(private val inDir: File, private val inDir2: File) : Supplier<Model?> {

    override fun get(): Model? = Factory(inDir, inDir2).get()

    companion object {

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

        val VERBOSE = false

        /**
         * Collect
         */
        fun CoreModel.fixMembersReference(): CoreModel {
            val orphans = orphanMembers()
            if (VERBOSE) {
                orphans.forEach { (lemma, synsets) ->
                    Tracing.psErr.println("[E] lemma $lemma member of  {${synsets.joinToString()}}")
                }
            }
            Tracing.psErr.println("[I] ${orphans.size} orphan entries")
            return fix(orphans)
        }

        fun CoreModel.orphanMembers0(): Map<Lemma, List<Synset>> {
            return synsets
                .map { synset -> synset to synset.members.filter { lexesByLemma!![it] == null }.toList() }
                .filter { (_, lemmas) -> lemmas.isNotEmpty() }
                .flatMap { (synset, lemmas) -> lemmas.map { lemma -> synset to lemma } }
                .groupBy({ it.second }, { it.first })
        }

        fun CoreModel.orphanMembers(): Map<Pair<Lemma, Char>, List<Synset>> {
            return synsets
                .map { synset -> synset to synset.members.filter { lexesByLemma!![it] == null || lexesByLemma!![it]?.none { lex -> lex.type == synset.type } ?: true }.toList() }
                .filter { (_, lemmas) -> lemmas.isNotEmpty() }
                .flatMap { (synset, lemmas) -> lemmas.map { lemma -> synset to (lemma to synset.type) } }
                .groupBy({ it.second }, { it.first })
        }

        // F I X

        /**
         * Fix
         */
        fun CoreModel.fix(pseudos: Map<Pair<Lemma, Category>, List<Synset>>): CoreModel {
            val newLexes = lexes.toMutableList()
            val newSenses = senses.toMutableList()
            pseudos.forEach { (typedLemma, synsets) ->
                val (lemma, type) = typedLemma
                val lex = Lex(lemma, type.toString(), source = source)
                lex.senseKeys = synsets.withIndex().map { (idx, synset) ->
                    val ssType: Category = synset.type
                    val senseid = "${escapeLemma(lemma)}%pseudo:$ssType:${idx + 1}"
                    val sense = Sense(senseid, lex, ssType, 0, synset.synsetId)
                    newSenses.add(sense)
                    senseid
                }.toList()
                newLexes.add(lex)
            }
            return CoreModel(newLexes, newSenses, synsets)
        }

        private fun escapeLemma(lemma: Lemma): String {
            return lemma.replace(' ', '_')
        }

        /**
         * Main
         *
         * @param args command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val model = makeModel(args)
            Tracing.psInfo.printf("[Model+] %s%n%s%n%s%n", model!!.sources.contentToString(), model.info(), ModelInfo.counts(model))
            // model.check()

            Tracing.psInfo.println("Check synset relation targets")
            model.checkSynsetRelationTargets()
            Tracing.psInfo.println("Check sense relation targets")
            model.checkSenseRelationTargets()
            Tracing.psInfo.println("Check members")
            model.checkMembers(verbose=false)

            val xModel = model
                .fixMembersReference()
                .checkMembers(verbose=true)
        }
    }
}