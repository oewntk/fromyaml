/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.model.*
import org.oewntk.model.Validator.check
import org.oewntk.yaml.`in`.LibTestsYamlPlusCommon.model
import org.oewntk.yaml.`in`.LibTestsYamlPlusCommon.ps

class TestsYamlPlusModel {

    @Test
    fun testModelInfo() {
        val info = model.info()
        val counts = ModelInfo.counts(model)
        ps.println("$info\n$counts")
    }

    private fun Lex.synsetIds(model: CoreModel): List<SynsetId> = senseKeys.map { model.senseResolver(it).synsetId }.toList()

    private fun Lex.toXString(model: CoreModel): String = "\t${this} -> ${synsetIds(LibTestsYamlPlusCommon.model)}"

    @Test
    fun testLexes() {
        testCases
            .forEach { testCase ->
                val (lemma: Lemma, pos: SynsetType, _: List<SynsetId>) = testCase
                val lexes = model.lexResolver(lemma)
                val byType: Map<SynsetType, Set<SynsetId>> = lexes.associateBy(
                    { it.type },
                    { lex: Lex ->
                        lex.senseKeys
                            .map { sk: SenseKey -> model.senseResolver(sk) }
                            .map { sense -> sense.synsetId } // { sense: Sense -> model.synsetResolver(sense.synsetId).synsetId} // or
                            .toSet()
                    })

                ps.println("$lemma -> ${lexes.joinToString(prefix = "{\n", postfix = "\n}", separator = ",\n") { it.toXString(model) }}")

                assert(byType.containsKey(pos)) { ps.print("k2=$pos") }
            }
    }

    @Test
    fun testLexesSenses() {
        testCases
            .forEach { testCase ->
                val (lemma: Lemma, _: SynsetType, synsetIds: List<SynsetId>) = testCase
                val lexes = model.lexResolver(lemma)
                val byPos: Map<SynsetType, Set<SynsetId>> = lexes.associateBy(
                    { it.type },
                    { lex: Lex ->
                        lex.senseKeys
                            .map { sk: SenseKey -> model.senseResolver(sk) }
                            .map { sense -> sense.synsetId } // { sense: Sense -> model.synsetResolver(sense.synsetId).synsetId} // or
                            .toSet()
                    })

                ps.println("$lemma -> ${lexes.joinToString(prefix = "{\n", postfix = "\n}", separator = ",\n") { it.toXString(model) }}")

                for (synsetId in synsetIds) {
                    assert(byPos.values.any { it.contains(synsetId) }) { ps.print("target synsetId=$synsetId") }
                }
            }
    }

    @Test
    fun testModel() {
        model.check(throws = true)
    }

    companion object {

        lateinit var testCases: List<Triple<Lemma, SynsetType, List<SynsetId>>>

        @JvmStatic
        @BeforeClass
        fun init() {
            testCases = requireNotNull(this::class.java.getResourceAsStream("/plus.log")).bufferedReader().useLines { lines ->
                lines
                    .map { line -> line.trim() }
                    .filter { line -> line.isEmpty() }
                    .map { line ->
                        val fields = line.split(";".toRegex(), limit = 3)
                        Triple(fields[0], SynsetType.fromChar(fields[1][0]), fields[2].split(","))
                    }
                    .toList()
            }

            model // eager
        }
    }
}
