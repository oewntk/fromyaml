/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.junit.BeforeClass
import org.junit.Ignore
import org.junit.Test
import org.oewntk.yaml.`in`.LibTestsYamlCommon.model
import org.oewntk.yaml.`in`.LibTestsYamlCommon.ps

@Ignore
class TestsYamlQuotes {

    @Test
    fun testQuote() {
        val synset = model!!.synsetsById?.get("06853940-n")!!
        synset.examples?.forEach {
            ps.println(it)
            assert(it.first.startsWith("`"))
        }
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
