/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.model.ModelInfo
import org.oewntk.yaml.`in`.LibTestsYamlPlusCommon.model
import org.oewntk.yaml.`in`.LibTestsYamlPlusCommon.ps

class TestsYamlPlusModel {

    @Test
    fun testModelInfo() {
        val info = model.info()
        val counts = ModelInfo.counts(model)
        ps.println("$info\n$counts")
    }

    companion object {

        @JvmStatic
        @BeforeClass
        fun init() {
            model
        }
    }
}
