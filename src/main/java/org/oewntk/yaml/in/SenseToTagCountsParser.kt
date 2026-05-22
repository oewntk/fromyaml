/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.oewntk.model.SenseKey
import org.oewntk.model.TagCount
import org.oewntk.yaml.`in`.YamlUtils.assertKeysIn
import java.io.File

/**
 * Sense-to-tag-count parser
 *
 * @param dir dir containing YAML/JSON files
 */
class SenseToTagCountsParser(dir: File, val fileext:String="yaml", verbose: Boolean = false) : YamProcessor1<Pair<String, TagCount>, String, Map<String, Int>>(dir, verbose = verbose) {

    override val files: Array<File>
        get() = dir.listFiles { f: File -> f.name.matches("senseToTagCounts.$fileext".toRegex()) }!!

    override fun processEntry(source: String?, entry: Pair<SenseKey, Map<String, Int>>): Pair<SenseKey, TagCount> {

        try {
            val sensekey = entry.first
            val tagCntMap = entry.second
            assertKeysIn(tagCntMap.keys, KEY_TAGCOUNT_SENSE_NUM, KEY_TAGCOUNT_COUNT)
            if (DUMP) {
                Tracing.psInfo.println(sensekey)
            }
            val senseNum = tagCntMap[KEY_TAGCOUNT_SENSE_NUM]!!
            val count = tagCntMap[KEY_TAGCOUNT_COUNT]!!
            return sensekey to TagCount(senseNum, count)
        } catch (iae: IllegalArgumentException) {
            throw IllegalArgumentException("${iae.message} in $source")
        }
    }

    companion object {

        private const val DUMP = false

        private const val KEY_TAGCOUNT_SENSE_NUM = "num"

        private const val KEY_TAGCOUNT_COUNT = "cnt"
    }
}
