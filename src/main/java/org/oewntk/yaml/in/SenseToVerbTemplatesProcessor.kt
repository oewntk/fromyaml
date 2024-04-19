/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.oewntk.model.SenseKey
import org.oewntk.model.VerbTemplateType
import java.io.File

/**
 * Sense-to-verb-templates processor
 *
 * @param dir dir containing YAML files
 */
class SenseToVerbTemplatesProcessor(dir: File) : YamProcessor1<Pair<SenseKey, Array<VerbTemplateType>>, String, List<VerbTemplateType>>(dir) {

	override val files: Array<File>
		get() = dir.listFiles { f: File -> f.name.matches("senseToVerbTemplates.yaml".toRegex()) }!!

	override fun processEntry(source: String?, entry: Pair<String, List<VerbTemplateType>>): Pair<String, Array<VerbTemplateType>>? {
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
