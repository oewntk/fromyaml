/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.oewntk.model.CoreModel
import org.oewntk.model.Lex.Companion.lexComparator
import java.io.File
import java.io.IOException
import java.util.Collections

/**
 * YAML parser that supplies model
 *
 * @property inDir dir containing YAML files
 */
class Parser(private val inDir: File, val fileext: String = "yaml", val verbose: Boolean = false) {

    /**
     * Parse
     *
     * @return core model
     * @throws IOException io exception
     */
    @Throws(IOException::class)
    fun parse(): CoreModel {

        // lexes + senses
        val lexParser = LexParser(inDir, fileext = fileext, verbose = verbose)
        val lexes = lexParser.parse().toSortedSet(lexComparator)
        val senses = lexParser.senses.toSortedSet()

        // synsets
        val synsetParser = SynsetParser(inDir, fileext = fileext, verbose = verbose)
        val synsets = synsetParser.parse().toSortedSet()

        return CoreModel(lexes, senses, synsets)
    }
}
