/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.oewntk.model.CoreModel
import java.io.File
import java.io.IOException

/**
 * YAML parser that supplies model
 *
 * @property inDir dir containing YAML files
 */
class Parser(private val inDir: File) {

    /**
     * Parse
     *
     * @return core model
     * @throws IOException io exception
     */
    @Throws(IOException::class)
    fun parse(): CoreModel {

        // lexes + senses
        val lexParser = LexParser(inDir)
        val lexes = lexParser.parse()
        val senses = lexParser.senses

        // synsets
        val synsetParser = SynsetParser(inDir)
        val synsets = synsetParser.parse()

        return CoreModel(lexes, senses, synsets)
    }
}
