/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.oewntk.model.Model
import org.oewntk.model.Validator.check
import java.io.File
import java.util.function.Supplier

/**
 * Model factory
 *
 * @param inDir  dir containing release YAML files
 * @param inDir2 dir containing extra YAML files
 */
class FactoryPlus(
    private val inDir: File,
    private val inDir2: File,
    private val fileext: String = "yaml",
    private val verbose: Boolean = false
) : Supplier<Model?> {

    override fun get(): Model? {
        val coreModel = CoreFactoryPlus(inDir, fileext = fileext, verbose = verbose).get()
        return coreModel?.let { Factory(inDir, inDir2, verbose = verbose).from(it) }
    }

    companion object {

        /**
         * Make model
         *
         * @param inDir  dir containing release YAML files
         * @param inDir2 dir containing extra YAML files
         * @return model
         */
        private fun makeModel(inDir: File, inDir2: File, verbose: Boolean = false): Model? {
            return FactoryPlus(inDir, inDir2, verbose = verbose).get()
        }

        /**
         * Make core model from YAML files
         *
         * @param args command-line arguments
         * @return core model
         */
        fun makeModel(args: Array<String>): Model? {
            var iArg = 0
            var verbose = false
            if (args[iArg] == "-verbose") {
                verbose = true
                iArg++
            }
            val inDir = File(args[iArg])
            val inDir2 = File(args[iArg + 1])
            return makeModel(inDir, inDir2, verbose = verbose)
        }

        /**
         * Main
         *
         * @param args command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val model = makeModel(args)
            if (model?.check(throws = false, verbose = true) == null) {
                Tracing.psErr.println("null model")
            }
        }
    }
}