/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.oewntk.model.VerbTemplate
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

	override fun processEntry(source: String?, entry: Map.Entry<Int, String>): VerbTemplate {
		val id = entry.key
		val v = entry.value
		if (DUMP) {
			Tracing.psInfo.println(id)
		}
		return VerbTemplate(id, v)
	}

	companion object {
		private const val DUMP = false
	}
}

