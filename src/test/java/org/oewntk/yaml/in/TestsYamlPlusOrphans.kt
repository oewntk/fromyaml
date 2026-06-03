/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.model.Lemma
import org.oewntk.model.SynsetId
import org.oewntk.model.SynsetType
import org.oewntk.yaml.`in`.CoreFactoryPlus.Companion.orphans
import org.oewntk.yaml.`in`.LibTestsYamlPlusCommon.model
import kotlin.test.assertEquals

class TestsYamlPlusOrphans {

    @Test
    fun testZuluOrphans() {
        val synset = model.synsetResolver("08506402-n") // Zulu community (group)
        val orphans = model.orphans(synset)
        for (o in orphans) {
            Tracing.psInfo.println(o)
        }
        assertEquals(0, orphans.size)
    }

    @Test
    fun testFixesPlus() {
        testCases
            .forEach { testCase ->
                val (_: Lemma, _: SynsetType, synsetIds: List<SynsetId>) = testCase
                synsetIds.forEach { synsetId ->
                    val synset = model.synsetResolver(synsetId)
                    val orphans = model.orphans(synset)
                    Tracing.psInfo.println(orphans)
                    assertEquals(0, orphans.size)
                }
            }
    }

    @Test
    fun testFixesPseudos() {
        pseudoCases
            .forEach { testCase ->
                val (_: Lemma, _: SynsetType, synsetIds: List<SynsetId>) = testCase
                synsetIds.forEach { synsetId ->
                    val synset = model.synsetResolver(synsetId)
                    val orphans = model.orphans(synset)
                    Tracing.psInfo.println(orphans)
                    assertEquals(0, orphans.size)
                }
            }
    }

    @Test
    fun testFixesGenerated() {
        generatedCases
            .forEach { testCase ->
                val (_: Lemma, _: SynsetType, synsetIds: List<SynsetId>) = testCase
                synsetIds.forEach { synsetId ->
                    val synset = model.synsetResolver(synsetId)
                    val orphans = model.orphans(synset)
                    Tracing.psInfo.println(orphans)
                    assertEquals(0, orphans.size)
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
                        Triple(fields[0], SynsetType.fromChar(fields[1][0]), fields[2].split(","))
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
                        Triple(fields[0], SynsetType.fromChar(fields[1][0]), fields[2].split(","))
                    }
                    .toList()
            }
        }

        val generatedCases: List<Triple<Lemma, SynsetType, List<SynsetId>>> by lazy {
            requireNotNull(this::class.java.getResourceAsStream("/generated.log")).bufferedReader().useLines { lines ->
                lines
                    .map { line -> line.trim() }
                    .filter { line -> line.isEmpty() }
                    .map { line ->
                        val fields = line.split(";".toRegex(), limit = 3)
                        Triple(fields[0], SynsetType.fromChar(fields[1][0]), fields[2].split(","))
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
