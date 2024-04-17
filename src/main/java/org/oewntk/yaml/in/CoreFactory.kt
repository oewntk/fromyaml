/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.oewntk.model.CoreModel
import java.io.File
import java.io.IOException
import java.util.function.Supplier

/**
 * Core model factory
 *
 * @property inDir dir containing YAML files
 */
class CoreFactory(private val inDir: File) : Supplier<CoreModel?> {

	override fun get(): CoreModel? {
		try {
			return Parser(inDir) //
				.parse() //
				.generateInverseRelations() //
				.setModelSource(inDir)
		} catch (e: IOException) {
			e.printStackTrace(Tracing.psErr)
			return null
		}
	}

	companion object {

		/**
		 * Make core model from YAML files
		 *
		 * @param args command-line arguments
		 * @return core model
		 */
		fun makeCoreModel(args: Array<String>): CoreModel? {
			val inDir = File(args[0])
			return CoreFactory(inDir).get()
		}

		/**
		 * Main
		 *
		 * @param args command-line arguments
		 */
		@JvmStatic
		fun main(args: Array<String>) {
			val model = makeCoreModel(args)
			Tracing.psInfo.printf("[CoreModel] %s%n%s%n%s%n", model!!.source, model.info(), model.counts())
		}
	}
}