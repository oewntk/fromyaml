/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.yaml.`in`.LexParser.Companion.VALID_SENSE_RELATIONS
import org.oewntk.yaml.`in`.YamlUtils.assertKeysIn
import java.io.PrintStream

class TestsAssertKeys {

    @Test
    fun testDoesNotThrow() {
        assertKeysIn(
            throws = false,
            testCases.keys,
            VALID_SENSE_RELATIONS,
            "id",
            "source",
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testThrows() {
        assertKeysIn(
            throws = true,
            testCases.keys,
            VALID_SENSE_RELATIONS,
            "id",
            "source",
        )
    }

    val testCases: Map<String, Any> =
        listOf(
            "antonym",
            "exemplifies",
            "rogue1",
            "exemplifies",
            "causes",
            "cause",
            "exemplify",
            "pertainym",
            "participle",
            "also",
            "domain_region",
            "domain_topic",
            "is_exemplified",
            "is_exemplified_by",
            "rogue2",
            "exmple",
            "id",
            "example"
        ).withIndex().associate { it.value to it.index }

    companion object {

        @Suppress("EmptyMethod")
        @JvmStatic
        @BeforeClass
        fun init() {
        }
    }
}
