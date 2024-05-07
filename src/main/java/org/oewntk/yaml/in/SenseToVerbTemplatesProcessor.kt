/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.oewntk.model.SenseKey
import org.oewntk.model.VerbTemplateId
import java.io.File

/**
 * Sense-to-verb-templates processor
 *
 * @param dir dir containing YAML files
 */
class SenseToVerbTemplatesProcessor(dir: File) : YamProcessor1<Pair<SenseKey, Array<VerbTemplateId>>, String, List<VerbTemplateId>>(dir) {

    override val files: Array<File>
        get() = dir.listFiles { f: File -> f.name.matches("senseToVerbTemplates.yaml".toRegex()) }!!

    override fun processEntry(source: String?, entry: Pair<String, List<VerbTemplateId>>): Pair<String, Array<VerbTemplateId>>? {
        val sensekey = entry.first
        val v = entry.second
        if (DUMP) {
            Tracing.psInfo.println(sensekey)
        }
        val n = v.size
        if (n == 0) {
            return null
        }
        val templateIds = Array(n) { v[it] }
        return sensekey to templateIds
    }

    companion object {

        private const val DUMP = false
    }
}
