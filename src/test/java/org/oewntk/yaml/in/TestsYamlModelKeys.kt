/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.model.LibTestModelKeys.testBaroqueMono
import org.oewntk.model.LibTestModelKeys.testBaroqueMulti
import org.oewntk.model.LibTestModelKeys.testBassDeep
import org.oewntk.model.LibTestModelKeys.testBassShallow
import org.oewntk.model.LibTestModelKeys.testCriticalMono
import org.oewntk.model.LibTestModelKeys.testCriticalMulti
import org.oewntk.model.LibTestModelKeys.testEarthMono
import org.oewntk.model.LibTestModelKeys.testEarthMulti
import org.oewntk.model.LibTestModelKeys.testMobile
import org.oewntk.model.LibTestModelKeys.testMobileNoPronunciation
import org.oewntk.model.LibTestModelKeys.testRowDeep
import org.oewntk.model.LibTestModelKeys.testRowShallow
import org.oewntk.yaml.`in`.LibTestsYamlCommon.model
import org.oewntk.yaml.`in`.LibTestsYamlCommon.ps

class TestsYamlModelKeys {

    @Test
    fun testMobile() {
        val r = testMobile(model!!, ps)
        assertEquals(1, r[0].toLong())
        assertEquals(1, r[1].toLong())
        assertEquals(1, r[2].toLong())
        assertEquals(2, r[3].toLong())
        assertEquals(2, r[4].toLong())
        assertEquals(5, r.size.toLong())
    }

    @Test
    fun testMobileNoPronunciation() {
        val r = testMobileNoPronunciation(model!!, ps)
        assertEquals(1, r[0].toLong())
        assertEquals(1, r[1].toLong())
        assertEquals(2, r[2].toLong())
        assertEquals(2, r[3].toLong())
        assertEquals(4, r.size.toLong())
    }

    @Test
    fun testEarthMulti() {
        val r = testEarthMulti(model!!, ps)
        assertEquals(1, r[0].toLong())
        assertEquals(1, r[1].toLong())
        assertEquals(2, r[2].toLong())
        assertEquals(2, r[3].toLong())
        assertEquals(4, r.size.toLong())
    }

    @Test
    fun testEarthMono() {
        val r = testEarthMono(model!!, ps)
        assertEquals(1, r[0].toLong())
        assertEquals(1, r[1].toLong())
        assertEquals(1, r[2].toLong())
        assertEquals(1, r[3].toLong())
        assertEquals(4, r.size.toLong())
    }

    @Test
    fun testBaroqueMulti() {
        val r = testBaroqueMulti(model!!, ps)
        assertEquals(1, r[0].toLong())
        assertEquals(1, r[1].toLong())
        assertEquals(2, r[2].toLong())
        assertEquals(2, r[3].toLong())
        assertEquals(1, r[4].toLong())
        assertEquals(1, r[5].toLong()) // <2> before s,a merging (p-request for s)
        assertEquals(2, r[6].toLong()) // <3> before s,a merging (p-request for s)
        assertEquals(2, r[7].toLong()) // <3> before s,a merging (p-request for s)
        assertEquals(0, r[8].toLong())
        assertEquals(0, r[9].toLong()) // <1> before s,a merging (t-request or s)
        assertEquals(0, r[10].toLong()) // <1> before s,a merging (t-request or s)
        assertEquals(0, r[11].toLong()) // <1> before s,a merging (t-request or s)
        assertEquals(0, r[12].toLong())
        assertEquals(0, r[13].toLong())
        assertEquals(0, r[14].toLong())
        assertEquals(0, r[14].toLong())
        assertEquals(16, r.size.toLong())
    }

    @Test
    fun testBaroqueMono() {
        val r = testBaroqueMono(model!!, ps)
        assertEquals(1, r[0].toLong())
        assertEquals(1, r[1].toLong())
        assertEquals(1, r[2].toLong())
        assertEquals(1, r[3].toLong())
        assertEquals(1, r[4].toLong())
        assertEquals(1, r[5].toLong())
        assertEquals(1, r[6].toLong())
        assertEquals(1, r[7].toLong())
        assertEquals(0, r[8].toLong())
        assertEquals(0, r[9].toLong()) // <1> before s,a merging (t-request for s)
        assertEquals(0, r[10].toLong()) // <1> before s,a merging (t-request for s)
        assertEquals(0, r[11].toLong()) // <1> before s,a merging (t-request for s)
        assertEquals(0, r[12].toLong())
        assertEquals(0, r[13].toLong())
        assertEquals(0, r[14].toLong())
        assertEquals(0, r[14].toLong())
        assertEquals(16, r.size.toLong())
    }

    @Test
    fun testCriticalMulti() {
        val r = testCriticalMulti(model!!, ps)
        assertEquals(1, r[0].toLong())
        assertEquals(1, r[1].toLong()) // <2> before a,s merging (p-request for a)
        assertEquals(0, r[2].toLong()) // <1> before a,s merging (t-request for s)
        assertEquals(0, r[3].toLong())
        assertEquals(4, r.size.toLong())
    }

    @Test
    fun testCriticalMono() {
        val r = testCriticalMono(model!!, ps)
        assertEquals(1, r[0].toLong())
        assertEquals(1, r[1].toLong())
        assertEquals(0, r[2].toLong()) // <2> before s,a merging (t-request for s)
        assertEquals(0, r[3].toLong())
        assertEquals(4, r.size.toLong())
    }

    @Test
    fun testBassDeep() {
        val r = testBassDeep(model!!, ps)
        assertEquals(1, r[0].toLong())
        assertEquals(1, r[1].toLong())
        assertEquals(2, r[2].toLong())
        assertEquals(3, r.size.toLong())
    }

    @Test
    fun testBassShallow() {
        val r = testBassShallow(model!!, ps)
        assertEquals(1, r[0].toLong())
        assertEquals(1, r[1].toLong())
        assertEquals(2, r[2].toLong())
        assertEquals(3, r.size.toLong())
    }

    @Test
    fun testRowDeep() {
        val r = testRowDeep(model!!, ps)
        assertEquals(1, r[0].toLong())
        assertEquals(1, r[1].toLong())
        assertEquals(2, r[2].toLong())
        assertEquals(3, r.size.toLong())
    }

    @Test
    fun testRowShallow() {
        val r = testRowShallow(model!!, ps)
        assertEquals(1, r[0].toLong())
        assertEquals(1, r[1].toLong())
        assertEquals(2, r[2].toLong())
        assertEquals(3, r.size.toLong())
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
