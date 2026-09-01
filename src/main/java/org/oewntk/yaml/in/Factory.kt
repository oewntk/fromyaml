/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.oewntk.model.*
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
    private val inDir: File,
    private val inDir2: File?,
    private val fileext: String = "yaml",
    private val fileext2: String = "yaml",
    private val throws: Boolean = true,
    private val inverses: Boolean = false,
    private val verbose: Boolean = false
) : Supplier<Model?> {

    data class Extra(
        val verbFrames: List<VerbFrame>,
        val verbTemplates: List<VerbTemplate>,
        val senseToVerbTemplates: Collection<Pair<SenseKey, List<VerbTemplateId>>>?,
        val senseToTagCounts: Collection<Pair<SenseKey, TagCount>>?,
    )

    override fun get(): Model? {
        val coreModel = CoreFactory(inDir, fileext = fileext, throws = throws, inverses = inverses, verbose = verbose).get() ?: return null
        return from(coreModel)
    }

    fun from(coreModel: CoreModel): Model? {
        return makeExtra()?.let {
            return Model(coreModel, it.verbFrames, it.verbTemplates, Injector(it.senseToVerbTemplates, it.senseToTagCounts), generatedInverses = coreModel.generatedInverses)
                .apply {
                    source = inDir.absolutePath
                    source2 = inDir2?.absolutePath
                }
        }
    }

    fun makeExtra(): Extra? {
        try {
            // verb frames and templates
            val verbFrames = VerbFrameParser(inDir, fileext = fileext, verbose = verbose).parse()
            val verbTemplates = VerbTemplateParser(inDir2 ?: inDir, fileext = fileext2, verbose = verbose).parse()

            // sense to verb templates
            val sensesToVerbTemplates = if (inDir2 != null) SenseToVerbTemplatesParser(inDir2, fileext = fileext2, verbose = verbose).parse() else null

            // tag counts
            val sensesToTagCounts = if (inDir2 != null) SenseToTagCountsParser(inDir2, fileext = fileext2, verbose = verbose).parse() else null

            return Extra(verbFrames, verbTemplates, sensesToVerbTemplates, sensesToTagCounts)

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
        private fun makeModel(inDir: File, inDir2: File?, fileext: String = "yaml", fileext2: String = "yaml", throws: Boolean = true, verbose: Boolean = false): Model? {
            return Factory(inDir, inDir2, fileext = fileext, fileext2 = fileext2, throws = throws, verbose = verbose).get()
        }

        /**
         * Make model
         *
         * @param dirPath1 WNDB dir path
         * @param dirPath2 extra WNDB dir path
         * @return core model
         */
        private fun makeModel(dirPath1: String, dirPath2: String?, fileext: String = "yaml", fileext2: String = "yaml", throws: Boolean = true, verbose: Boolean = false): Model? {
            val inDir = File(dirPath1)
            val inDir2 = if (dirPath2 == null) null else File(dirPath2)
            return makeModel(inDir, inDir2, fileext = fileext, fileext2 = fileext2, throws = throws, verbose = verbose)
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
            var verbose = false
            if ("--verbose" == args[iArg]) {
                verbose = true
                iArg++
            }
            if ("--json" == args[iArg]) {
                fileext = "json"
                iArg++
            }
            var fileext2 = "yaml"
            if ("--json2" == args[iArg]) {
                fileext2 = "json"
                iArg++
            }
            var throws = true
            if ("--no-throw" == args[iArg]) {
                throws = false
                iArg++
            }
            val inDir = args[iArg]
            iArg++
            val inDir2 = if (iArg < args.size) args[iArg] else null
            return makeModel(inDir, inDir2, fileext = fileext, fileext2 = fileext2, throws = throws, verbose = verbose)
        }

        /**
         * Main
         *
         * @param args command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val model = makeModel(args)
            Tracing.psInfo.printf("[Model] %s%n%s%n%s%n", model!!, model.info(), ModelInfo.counts(model))
        }
    }
}