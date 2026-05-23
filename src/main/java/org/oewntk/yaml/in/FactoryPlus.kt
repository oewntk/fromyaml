/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.oewntk.model.Model
import org.oewntk.model.check
import java.io.File
import java.util.function.Supplier

/**
 * Model factory
 *
 * @param inDir  dir containing release YAML files
 * @param inDir2 dir containing extra YAML files
 */
class FactoryPlus(private val inDir: File, private val inDir2: File, val fileext: String = "yaml", val verbose: Boolean = false) : Supplier<Model?> {

    override fun get(): Model? {
        val coreModel = CoreFactoryPlus(inDir).get()
        return coreModel?.let { Factory(inDir, inDir2).from(it) }
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
            return FactoryPlus(inDir, inDir2).get()
        }

        /**
         * Make core model from YAML files
         *
         * @param args command-line arguments
         * @return core model
         */
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
            if (model?.check(verbose = true) == null) {
                Tracing.psErr.println("null model")
            }
        }
    }
}