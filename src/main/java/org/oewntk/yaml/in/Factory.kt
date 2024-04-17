/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.oewntk.model.Model
import org.oewntk.model.TagCount
import java.io.File
import java.io.IOException
import java.util.function.Supplier

/**
 * Model factory
 *
 * @param inDir  dir containing release YAML files
 * @param inDir2 dir containing extra YAML files
 */
class Factory(private val inDir: File, private val inDir2: File) : Supplier<Model?> {

	override fun get(): Model? {
		val coreModel = CoreFactory(inDir).get() ?: return null

		try {
			// verb frames and templates
			val verbFrames = VerbFrameProcessor(inDir).parse()
			val verbTemplates = VerbTemplateProcessor(
				inDir2
			).parse()
			val sensesToVerbTemplates = SenseToVerbTemplatesProcessor(
				inDir2
			).parse()

			// tag counts
			val sensesToTagCounts: Collection<Map.Entry<String, TagCount>> = SenseToTagCountsProcessor(
				inDir2
			).parse()

			return Model(coreModel, verbFrames, verbTemplates, sensesToVerbTemplates, sensesToTagCounts).setSources(
				inDir, inDir2
			)
		} catch (e: IOException) {
			e.printStackTrace(Tracing.psErr)
			return null
		}
	}

	companion object {

		/**
		 * Make model
		 *
		 * @param inDir  dir containing release YAML files
		 * @param inDir2 dir containing extra YAML files
		 * @return model
		 */
		private fun makeModel(inDir: File, inDir2: File): Model? {
			return Factory(inDir, inDir2).get()
		}

		/**
		 * Make core model from YAML files
		 *
		 * @param args command-line arguments
		 * @return core model
		 */
		@JvmStatic
		fun makeModel(args: Array<String>): Model? {
			val inDir = File(args[0])
			val inDir2 = File(args[1])
			return makeModel(inDir, inDir2)
		}

		/**
		 * Main
		 *
		 * @param args command-line arguments
		 */
		@JvmStatic
		fun main(args: Array<String>) {
			val model = makeModel(args)
			Tracing.psInfo.printf(
				"[Model] %s%n%s%n%s%n",
				model!!.sources.contentToString(),
				model.info(),
				model.counts()
			)
		}
	}
}