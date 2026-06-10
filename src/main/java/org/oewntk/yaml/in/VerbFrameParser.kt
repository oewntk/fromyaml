/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.oewntk.model.SenseKey
import org.oewntk.model.VerbFrame
import org.oewntk.model.VerbFrameId
import java.io.File

/**
 * Verb frames parser
 *
 * @param dir dir containing YAML/JSON files
 */
class VerbFrameParser(dir: File, val fileext: String = "yaml", verbose: Boolean = false) : YamlProcessor1<VerbFrame, String, String>(dir, verbose = verbose) {

    init {
        this.dir = dir
    }

    override val files: Array<File>
        get() = dir.listFiles { f: File -> f.name.matches("frames.$fileext".toRegex()) } ?: arrayOf()

    override fun processEntry(source: String?, entry: Pair<SenseKey, VerbFrameId>): VerbFrame {
        val id = entry.first
        val v = entry.second
        if (DUMP) {
            Tracing.psInfo.println(id)
        }
        return VerbFrame(id, v)
    }

    companion object {

        private const val DUMP = false
    }
}
