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
import org.oewntk.yaml.`in`.LibTestsYamlStubCommon.model
import kotlin.test.assertEquals

class TestsYamlStubOrphans {

    @Test
    fun testZuluOrphan() {
        val synset = model.synsetResolver("08506402-n") // Zulu community (group)
        val orphans = model.orphans(synset)
        Tracing.psInfo.println(orphans)
        assertEquals(1, orphans.size)
        assertEquals("Zulu", orphans[0])
    }

    @Test
    fun testOrphans() {
        testCases
            .forEach { testCase ->
                val (lemma: Lemma, _: SynsetType, synsetIds: List<SynsetId>) = testCase
                synsetIds.forEach { synsetId ->
                    val synset = model.synsetResolver(synsetId)
                    val orphans = model.orphans(synset)
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
