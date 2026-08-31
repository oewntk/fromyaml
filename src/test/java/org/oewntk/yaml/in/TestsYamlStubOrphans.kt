/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.model.Lemma
import org.oewntk.model.SynsetId
import org.oewntk.model.SynsetType
import org.oewntk.yaml.`in`.CoreFactoryPlus.Companion.orphanMembers
import org.oewntk.yaml.`in`.LibTestsYamlStubCommon.model
import kotlin.test.assertEquals

class TestsYamlStubOrphans {

    @Test
    fun testZuluOrphan() {
        val synset = model.synsetResolver(SynsetId("08506402-n")) // Zulu community (group)
        val orphans = model.orphanMembers(synset)
        Tracing.psInfo.println(orphans)
        assertEquals(1, orphans.size)
        assertEquals(Lemma("Zulu"), orphans[0])
    }

    @Test
    fun testOrphans() {
        testCases
            .forEach { testCase ->
                val (lemma: Lemma, _: SynsetType, synsetIds: List<SynsetId>) = testCase
                synsetIds.forEach { synsetId ->
                    val synset = model.synsetResolver(synsetId)
                    val orphans = model.orphanMembers(synset)
                    Tracing.psInfo.println(orphans)
                    assertEquals(1, orphans.size)
                    assertEquals(lemma, orphans[0])
                }
            }
    }

    @Test
    fun testFailsPlus() {
        testCases
            .forEach { testCase ->
                val (lemma: Lemma, _: SynsetType, synsetIds: List<SynsetId>) = testCase
                synsetIds.forEach { synsetId ->
                    val synset = model.synsetResolver(synsetId)
                    val orphans = model.orphanMembers(synset)
                    Tracing.psInfo.println(orphans)
                    assertEquals(1, orphans.size)
                    assertEquals(lemma, orphans[0])
                }
            }
    }

    @Test
    fun testFailsPseudos() {
        pseudoCases
            .forEach { testCase ->
                val (lemma: Lemma, _: SynsetType, synsetIds: List<SynsetId>) = testCase
                synsetIds.forEach { synsetId ->
                    val synset = model.synsetResolver(synsetId)
                    val orphans = model.orphanMembers(synset)
                    Tracing.psInfo.println(orphans)
                    assertEquals(1, orphans.size)
                    assertEquals(lemma, orphans[0])
                }
            }
    }

    @Test
    fun testFailsGenerated() {
        generatedCases
            .forEach { testCase ->
                val (lemma: Lemma, _: SynsetType, synsetIds: List<SynsetId>) = testCase
                synsetIds.forEach { synsetId ->
                    val synset = model.synsetResolver(synsetId)
                    val orphans = model.orphanMembers(synset)
                    Tracing.psInfo.println(orphans)
                    assertEquals(1, orphans.size)
                    assertEquals(lemma, orphans[0])
                }
            }
    }

    companion object {

        val testCases: List<Triple<Lemma, SynsetType, List<SynsetId>>> by lazy {
            requireNotNull(this::class.java.getResourceAsStream("/plus.log")).bufferedReader().useLines { lines ->
                lines
                    .map { line -> line.trim() }
                    .filter { line -> line.isEmpty() }
                    .map { line ->
                        val fields = line.split(";".toRegex(), limit = 3)
                        Triple(Lemma(fields[0]), SynsetType.fromChar(fields[1][0]), fields[2].split(",").map { SynsetId(it) })
                    }
                    .toList()
            }
        }

        val pseudoCases: List<Triple<Lemma, SynsetType, List<SynsetId>>> by lazy {
            requireNotNull(this::class.java.getResourceAsStream("/pseudos_curated.log")).bufferedReader().useLines { lines ->
                lines
                    .map { line -> line.trim() }
                    .filter { line -> line.isEmpty() }
                    .map { line ->
                        val fields = line.split(";".toRegex(), limit = 3)
                        Triple(Lemma(fields[0]), SynsetType.fromChar(fields[1][0]), fields[2].split(",").map { SynsetId(it) })
                    }
                    .toList()
            }
        }

        val generatedCases: List<Triple<Lemma, SynsetType, List<SynsetId>>> by lazy {
            requireNotNull(this::class.java.getResourceAsStream("/plus.log")).bufferedReader().useLines { lines ->
                lines
                    .map { line -> line.trim() }
                    .filter { line -> line.isEmpty() }
                    .map { line ->
                        val fields = line.split(";".toRegex(), limit = 3)
                        Triple(Lemma(fields[0]), SynsetType.fromChar(fields[1][0]), fields[2].split(",").map { SynsetId(it) })
                    }
                    .toList()
            }
        }

        @JvmStatic
        @BeforeClass
        fun init() {

            model // eager
        }
    }
}
