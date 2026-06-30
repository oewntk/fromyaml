/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.oewntk.model.CoreModel
import org.oewntk.model.ModelInfo
import org.oewntk.model.Validator.check
import java.io.File
import java.util.function.Supplier

/**
 * Model factory
 *
 * @param inDir  dir containing release YAML files
 */
open class CoreProtoFactoryPlus(
    private val inDir: File,
    private val fileext: String = "yaml",
    private val throws: Boolean = true,
    private val verbose: Boolean = false
) : Supplier<CoreModel?> {

    override fun get(): CoreModel? {
        return make(inDir)
    }

    private fun make(inDir: File): CoreModel? {
        val stubModel: CoreModel? = CoreFactory(inDir, fileext = fileext, throws = throws, verbose = false).get()
        return stubModel?.let { model ->
            if (verbose) Tracing.psInfo.printf("[StubModel] %s%n%s%n%s%n", model.source, model.info(), ModelInfo.counts(stubModel))
            return model
        }
    }

    companion object {

        /**
         * Make model
         *
         * @param inDir  dir containing release YAML files
         * @return model
         */
        private fun makeModel(inDir: File, throws: Boolean = true, verbose: Boolean = false): CoreModel? {
            return CoreProtoFactoryPlus(inDir, throws = throws, verbose = verbose).get()
        }

        /**
         * Make core model from YAML files
         *
         * @param args command-line arguments
         * @return core model
         */
        private fun makeModel(args: Array<String>): CoreModel? {
            var iArg = 0
            var verbose = false
            var doNotThrow = false
            if (args[iArg] == "--verbose") {
                verbose = true
                iArg++
            }
            if ("--no-throw" == args[iArg]) {
                doNotThrow = true
                iArg++
            }
            val inDir = File(args[iArg])
            return makeModel(inDir, throws = !doNotThrow, verbose = verbose)
        }

        /**
         * Main
         *
         * @param args command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val model = makeModel(args)
            if (model?.check(verbose = true) == null) {
                Tracing.psErr.println("null model")
            }
        }
    }
}