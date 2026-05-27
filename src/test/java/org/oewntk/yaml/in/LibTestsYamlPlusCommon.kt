/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.junit.Assert
import org.oewntk.model.CoreModel
import java.io.File
import java.io.PrintStream

object LibTestsYamlPlusCommon {

    private val source: String? = System.getProperty("SOURCEPLUS")

    val silent =  !System.getProperties().containsKey("SILENT")

    val ps: PrintStream = if (!silent) Tracing.psInfo else Tracing.psNull

    val model: CoreModel by lazy {
        if (source == null) {
            Tracing.psErr.println("Define yamlplus PLUS source dir with -DSOURCEPLUS=path")
            throw AssertionError("SOURCEPLUS not defined")
        }
        val inDir = File(source)
        Tracing.psInfo.printf("source=%s%n", inDir.absolutePath)
        if (!inDir.exists()) {
            Tracing.psErr.println("Define YAML source dir that exists")
            Assert.fail()
        }
        CoreFactoryPlus(inDir, verbose = !silent).get()!!
    }
}
