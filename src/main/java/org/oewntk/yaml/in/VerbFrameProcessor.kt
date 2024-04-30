/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.oewntk.model.SenseKey
import org.oewntk.model.VerbFrame
import org.oewntk.model.VerbFrameType
import java.io.File

/**
 * Verb frames processor
 *
 * @param dir dir containing YAML files
 */
class VerbFrameProcessor(dir: File) : YamProcessor1<VerbFrame, String, String>(dir) {

    init {
        this.dir = dir
    }

    override val files: Array<File>
        get() = dir.listFiles { f: File -> f.name.matches("frames.yaml".toRegex()) }!!

    override fun processEntry(source: String?, entry: Pair<SenseKey, VerbFrameType>): VerbFrame {
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
