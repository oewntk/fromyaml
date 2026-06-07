/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.oewntk.model.VerbTemplate
import org.oewntk.model.VerbTemplateId
import java.io.File

/**
 * Verb Templates parser
 *
 * @property dir dir containing YAML/JSON files
 */
class VerbTemplateParser(dir: File, val fileext:String="yaml", verbose: Boolean = false) : YamProcessor1<VerbTemplate, Int, String>(dir, verbose = verbose) {

    init {
        this.dir = dir
    }

    override val files: Array<File>
        get() = dir.listFiles { f -> f.name.matches("templates.$fileext".toRegex()) } ?: arrayOf()

    override fun processEntry(source: String?, entry: Pair<VerbTemplateId, String>): VerbTemplate {
        val id = entry.first
        val v = entry.second
        if (DUMP) {
            Tracing.psInfo.println(id)
        }
        return VerbTemplate(id, v)
    }

    companion object {

        private const val DUMP = false
    }
}

