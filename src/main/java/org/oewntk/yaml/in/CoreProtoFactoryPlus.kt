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
    private val verbose: Boolean = false
) : Supplier<CoreModel?> {

    override fun get(): CoreModel? {
        return make(inDir)
    }

    private fun make(inDir: File): CoreModel? {
        val stubModel: CoreModel? = CoreFactory(inDir, fileext = fileext, throws = false, verbose = false).get()
        return stubModel?.let { model ->
            if (verbose) Tracing.psInfo.printf("[StubModel] %s%n%s%n%s%n", model.source, model.info(), ModelInfo.counts(stubModel))
            return model
                .check(throws = false, verbose = verbose)
        }
    }

    companion object {

        /**
         * Make model
         *
         * @param inDir  dir containing release YAML files
         * @return model
         */
        private fun makeModel(inDir: File): CoreModel? {
            return CoreProtoFactoryPlus(inDir).get()
        }

        /**
         * Make core model from YAML files
         *
         * @param args command-line arguments
         * @return core model
         */
        fun makeModel(args: Array<String>): CoreModel? {
            val inDir = File(args[0])
            return makeModel(inDir)
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