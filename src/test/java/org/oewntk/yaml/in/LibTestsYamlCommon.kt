/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.junit.Assert
import org.oewntk.model.CoreModel
import java.io.File
import java.io.PrintStream

object LibTestsYamlCommon {

	private val source: String? = System.getProperty("SOURCE")

	@JvmField
	val ps: PrintStream = if (!System.getProperties().containsKey("SILENT")) Tracing.psInfo else Tracing.psNull

	@JvmField
	var model: CoreModel? = null

	@JvmStatic
	fun init() {
		if (model == null) {
			if (source == null) {
				Tracing.psErr.println("Define YAML source dir with -DSOURCE=path%n")
				Tracing.psErr.println("When running Maven tests, define the yaml directory as child to the project directory.")
				Assert.fail()
			}
			val inDir = File(source!!)
			Tracing.psInfo.printf("source=%s%n", inDir.absolutePath)
			if (!inDir.exists()) {
				Tracing.psErr.println("Define YAML source dir that exists")
				Assert.fail()
			}
			model = CoreFactory(inDir).get()
		}
		checkNotNull(model)
		ps.println(model!!.info())
		ps.println(model!!.counts())
	}
}
