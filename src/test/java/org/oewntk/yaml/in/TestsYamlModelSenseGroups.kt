/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.model.LibTestModelSenseGroups.testCISensesGroupingByLCLemma
import org.oewntk.model.LibTestModelSenseGroups.testCISensesGroupingByLCLemmaAndPos
import org.oewntk.yaml.`in`.LibTestsYamlCommon.model
import org.oewntk.yaml.`in`.LibTestsYamlCommon.ps

class TestsYamlModelSenseGroups {

    @Test
    fun testCISensesBaroque() {
        testCISensesGroupingByLCLemma(model!!, "baroque", ps)
    }

    @Test
    fun testCISensesBaroqueA() {
        testCISensesGroupingByLCLemmaAndPos(model!!, "baroque", 'a', ps)
    }

    @Test
    fun testCISensesCriticalA() {
        testCISensesGroupingByLCLemmaAndPos(model!!, "critical", 'a', ps)
    }

    @Test
    fun testCISensesAiN() {
        testCISensesGroupingByLCLemmaAndPos(model!!, "ai", 'n', ps)
    }

    @Test
    fun testCISensesAbsoluteA() {
        testCISensesGroupingByLCLemmaAndPos(model!!, "absolute", 'a', ps)
    }

    companion object {

        @JvmStatic
        @BeforeClass
        fun init() {
            LibTestsYamlCommon.init()
            checkNotNull(model)
        }
    }
}
