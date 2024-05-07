/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.oewntk.model.VerbTemplate
import org.oewntk.model.VerbTemplateId
import java.io.File

/**
 * Verb Templates processor
 *
 * @property dir dir containing YAML files
 */
class VerbTemplateProcessor(dir: File) : YamProcessor1<VerbTemplate, Int, String>(dir) {

    init {
        this.dir = dir
    }

    override val files: Array<File>
        get() = dir.listFiles { f -> f.name.matches("verbTemplates.yaml".toRegex()) }!!

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

