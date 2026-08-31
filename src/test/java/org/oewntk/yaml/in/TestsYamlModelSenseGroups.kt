/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.model.Category
import org.oewntk.model.Lemma
import org.oewntk.model.LibTestModelSenseGroups.testCISensesGroupingByLCLemma
import org.oewntk.model.LibTestModelSenseGroups.testCISensesGroupingByLCLemmaAndPos
import org.oewntk.yaml.`in`.LibTestsYamlCommon.model
import org.oewntk.yaml.`in`.LibTestsYamlCommon.ps

class TestsYamlModelSenseGroups {

    @Test
    fun testCISensesBaroque() {
        testCISensesGroupingByLCLemma(model, Lemma("baroque"), ps)
    }

    @Test
    fun testCISensesBaroqueA() {
        testCISensesGroupingByLCLemmaAndPos(model, Lemma("baroque"), Category.A, ps)
    }

    @Test
    fun testCISensesCriticalA() {
        testCISensesGroupingByLCLemmaAndPos(model, Lemma("critical"), Category.A, ps)
    }

    @Test
    fun testCISensesAiN() {
        testCISensesGroupingByLCLemmaAndPos(model, Lemma("ai"), Category.N, ps)
    }

    @Test
    fun testCISensesAbsoluteA() {
        testCISensesGroupingByLCLemmaAndPos(model, Lemma("absolute"), Category.A, ps)
    }

    companion object {

        @JvmStatic
        @BeforeClass
        fun init() {
            model
        }
    }
}
