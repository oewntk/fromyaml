/*
 * Copyright (c) 2021-2024. Bernard Bou.
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
class Parser(private val inDir: File, val fileext: String = "yaml", val verbose: Boolean = false) {

    /**
     * Parse
     * calling toSortedSet() results in TreeSet
     * which poses problems with serialization
     * (TreeSet's comparator must be serializable)
     *
     * @return core model
     * @throws IOException io exception
     */
    @Throws(IOException::class)
    fun parse(): CoreModel {

        // lexes + senses
        val lexParser = LexParser(inDir, fileext = fileext, verbose = verbose)
        val lexes = lexParser.parse().sorted().distinct()
        val senses = lexParser.senses.sorted().distinct()

        // synsets
        val synsetParser = SynsetParser(inDir, fileext = fileext, verbose = verbose)
        val synsets = synsetParser.parse().sorted().distinct()

        return CoreModel(lexes, senses, synsets)
    }
}
