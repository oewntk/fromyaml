/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.oewntk.model.SenseKey
import org.oewntk.model.TagCount
import org.oewntk.yaml.`in`.YamlUtils.assertKeysIn
import java.io.File

/**
 * Sense-to-tag-count processor
 *
 * @param dir dir containing YAML files
 */
class SenseToTagCountsProcessor(dir: File) : YamProcessor1<Pair<String, TagCount>, String, Map<String, Int>>(dir) {

    override val files: Array<File>
        get() = dir.listFiles { f: File -> f.name.matches("senseToTagCounts.yaml".toRegex()) }!!

    override fun processEntry(source: String?, entry: Pair<SenseKey, Map<String, Int>>): Pair<SenseKey, TagCount> {

        val sensekey = entry.first
        val tagCntMap = entry.second
        assertKeysIn(source!!, tagCntMap.keys, KEY_TAGCOUNT_SENSE_NUM, KEY_TAGCOUNT_COUNT)
        if (DUMP) {
            Tracing.psInfo.println(sensekey)
        }
        val senseNum = tagCntMap[KEY_TAGCOUNT_SENSE_NUM]!!
        val count = tagCntMap[KEY_TAGCOUNT_COUNT]!!
        return sensekey to TagCount(senseNum, count)
    }

    companion object {

        private const val DUMP = false

        private const val KEY_TAGCOUNT_SENSE_NUM = "num"

        private const val KEY_TAGCOUNT_COUNT = "cnt"
    }
}
