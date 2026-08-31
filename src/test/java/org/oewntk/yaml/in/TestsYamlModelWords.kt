/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.model.Key
import org.oewntk.model.Lemma
import org.oewntk.model.Lex
import org.oewntk.model.LibTestModel.makeIndexMap
import org.oewntk.model.LibTestModel.makeSortedIndexMap
import org.oewntk.model.LibTestModel.testScanLexesForTestWords
import org.oewntk.model.LibTestModel.testWord
import org.oewntk.model.LibTestModel.testWords
import org.oewntk.model.PartOfSpeech
import org.oewntk.yaml.`in`.LibTestsYamlCommon.model
import org.oewntk.yaml.`in`.LibTestsYamlCommon.ps

class TestsYamlModelWords {

    @Test
    fun testScanLexesForTestWords() {
        testScanLexesForTestWords(model, { lex: Lex -> Key.UsingPronunciation.of(lex) }, { seq: Sequence<Key> -> makeIndexMap(seq) }, testWords, PRINT_TEST_WORDS, ps)
    }

    @Test
    fun testScanLexesForTestWordsSorted() {
        testScanLexesForTestWords(model, { lex: Lex -> Key.UsingPronunciation.of(lex) }, { seq: Sequence<Key> -> makeSortedIndexMap(seq) }, testWords, PRINT_TEST_WORDS, ps)
    }

    @Test
    fun testBass() {
        testWord(Lemma("bass"), model, ps)
    }

    @Test
    fun testRow() {
        testWord(Lemma("row"), model, ps)
    }

    @Test
    fun testCentre() {
        testWord(Lemma("centre"), model, ps)
    }

    @Test
    fun testBaroque() {
        testWords(model, ps, arrayOf(Lemma("baroque"), Lemma("Baroque")))
    }

    @Test
    fun testEarth() {
        testWords(model, ps, arrayOf(Lemma("earth"), Lemma("Earth")))
    }

    @Test
    fun testCritical() {
        testWord(Lemma("critical"), PartOfSpeech.A, model, ps)
    }

    @Test
    fun testHollywood() {
        testWord(Lemma("Hollywood"), PartOfSpeech.A, model, ps)
    }

    @Test
    fun testVictorian() {
        testWord(Lemma("Victorian"), PartOfSpeech.A, model, ps)
    }

    @Test
    fun testAllied() {
        testWord(Lemma("allied"), PartOfSpeech.A, model, ps)
    }

    @Test
    fun testAlliedUpper() {
        testWord(Lemma("Allied"), PartOfSpeech.A, model, ps)
    }

    @Test
    fun testAbsent() {
        testWord(Lemma("absent"), PartOfSpeech.A, model, ps)
    }

    @Test
    fun testApocryphal() {
        testWord(Lemma("apocryphal"), PartOfSpeech.A, model, ps)
    }

    @Test
    fun testUsed() {
        testWord(Lemma("used"), PartOfSpeech.A, model, ps)
    }

    companion object {

        private const val PRINT_TEST_WORDS = false

        private val testWords = setOf(Lemma("baroque"), Lemma("Baroque"), Lemma("allied"), Lemma("Allied"), Lemma("earth"), Lemma("Earth"), Lemma("bass"), Lemma("row"), Lemma("critical"), Lemma("centre"))

        @JvmStatic
        @BeforeClass
        fun init() {
            model
        }
    }
}
