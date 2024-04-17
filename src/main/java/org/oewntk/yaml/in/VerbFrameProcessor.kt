/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.oewntk.model.VerbFrame
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

	override fun processEntry(source: String?, entry: Map.Entry<String, String>): VerbFrame {
		val id = entry.key
		val v = entry.value
		if (DUMP) {
			Tracing.psInfo.println(id)
		}
		return VerbFrame(id, v)
	}

	companion object {
		private const val DUMP = false
	}
}
