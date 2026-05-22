/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.oewntk.model.Model
import org.oewntk.model.ModelInfo
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
class Factory(
    private val inDir: File, private val inDir2: File,
    val fileext: String = "yaml",
    val fileext2: String = "yaml",
    val verbose: Boolean = false
) : Supplier<Model?> {

    override fun get(): Model? {
        val coreModel = CoreFactory(inDir, fileext = fileext, verbose = verbose).get() ?: return null

        try {
            // verb frames and templates
            val verbFrames = VerbFrameParser(inDir, fileext = fileext).parse()
            val verbTemplates = VerbTemplateParser(inDir2, fileext = fileext2).parse()
            val sensesToVerbTemplates = SenseToVerbTemplatesParser(inDir2, fileext = fileext2).parse()

            // tag counts
            val sensesToTagCounts: Collection<Pair<String, TagCount>> = SenseToTagCountsParser(inDir2, fileext = fileext2).parse()

            return Model(coreModel, verbFrames, verbTemplates, sensesToVerbTemplates, sensesToTagCounts)
                .apply {
                    source = inDir.absolutePath
                    source2 = inDir2.absolutePath
                }

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
        private fun makeModel(inDir: File, inDir2: File, fileext: String = "yaml", fileext2: String = "yaml", verbose: Boolean = true): Model? {
            return Factory(inDir, inDir2, fileext = fileext, fileext2 = fileext2, verbose = verbose).get()
        }

        /**
         * Make core model from YAML files
         *
         * @param args command-line arguments
         * @return core model
         */
        fun makeModel(args: Array<String>): Model? {
            var iArg = 0
            var fileext = "yaml"
            if ("--json" == args[iArg]) {
                fileext = "json"
                iArg++
            }
            var fileext2 = "yaml"
            if ("--json2" == args[iArg]) {
                fileext2 = "json"
                iArg++
            }
            val inDir = File(args[iArg])
            val inDir2 = File(args[iArg+1])
            return makeModel(inDir, inDir2, fileext = fileext, fileext2 = fileext2, )
        }

        /**
         * Main
         *
         * @param args command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val model = makeModel(args)
            Tracing.psInfo.printf("[Model] %s%n%s%n%s%n", model!!.sources.contentToString(), model.info(), ModelInfo.counts(model))
        }
    }
}