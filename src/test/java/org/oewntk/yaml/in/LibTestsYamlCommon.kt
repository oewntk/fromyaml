/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.junit.Assert
import org.oewntk.model.Model
import org.oewntk.model.ModelInfo
import java.io.File
import java.io.PrintStream
import kotlin.test.assertEquals

object LibTestsYamlCommon {

    private val source: String? = System.getProperty("SOURCE")
    private val source2: String? = System.getProperty("SOURCE2")

    private val sourceB: String? = System.getProperty("SOURCEB")
    private val sourceB2: String? = System.getProperty("SOURCEB2")

    val silent = !System.getProperties().containsKey("VERBOSE") && if (System.getProperties().containsKey("SILENT")) true
    else true

    val ps: PrintStream = if (!silent) Tracing.psInfo else Tracing.psNull

    fun checkOrig() {
        val orig: String = System.getProperty("INFO")!!
        val origInfo = File(orig).readText()
        val info = model.info()
        val counts = ModelInfo.counts(model)
        val modelInfo = "$info\n$counts"
        ps.println(modelInfo)
        assertEquals(origInfo, modelInfo)
    }

    private fun getModel(source: String, source2: String?): Model {
        val inDir = File(source)
        val inDir2 = if (source2 == null) null else File(source2)
        Tracing.psInfo.printf("source=%s%n", inDir.absolutePath)
        if (!inDir.exists()) {
            Tracing.psErr.println("Define YAML source dir that exists")
            Assert.fail()
        }
        return Factory(inDir, inDir2, throws = true, inverses = false, verbose = !silent).get()!!
    }

    val model: Model by lazy {
        if (source == null) {
            Tracing.psErr.println("Define serialized source file dir with -DSOURCE=path")
            throw AssertionError("SOURCE not defined")
        }
        getModel(source, source2)
    }

    val modelB: Model by lazy {
        if (sourceB == null) {
            Tracing.psErr.println("Define serialized source file dir with -DSOURCEB=path")
            throw AssertionError("SOURCEB not defined")
        }
        getModel(sourceB, sourceB2)
    }
}
