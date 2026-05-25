/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.yaml.`in`.YamlUtils.processExampleText
import org.oewntk.yaml.`in`.YamlUtils.processExampleTextOrThrow
import java.io.PrintStream

class TestsExamples {

    val ps: PrintStream = if (!System.getProperties().containsKey("SILENT")) Tracing.psInfo else Tracing.psNull

    private val enclosed = "\"Israel has been the fastest to roll out inoculations.\""
    private val even = "Examples of front vowels include \"a\" in \"man\" and \"e\" in \"gel\""
    private val uneven = "Examples of front vowels include \"a\" in \"man\" and \"e in \"gel\""
    private val spaces = "  Examples of front vowels include \"a\" in \"man\" and \"e in \"gel\"    "

    private fun processAndFix(example: String): String {
        ps.println("<$example>")
        val processed = processExampleText(example)
        ps.println("<$processed>")
        return processed
    }

    private fun process(example: String) {
        try {
            processExampleTextOrThrow(example)
        } catch (iae: IllegalArgumentException) {
            ps.println("<$example>")
            throw iae
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun testEnclosingQuotes() {
        process(enclosed)
    }

    @Test
    fun testEvenQuotes() {
        process(even)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testUnEvenQuotes() {
        process(uneven)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testSpaces() {
        process(spaces)
    }

    companion object {

        @Suppress("EmptyMethod")
        @JvmStatic
        @BeforeClass
        fun init() {
        }
    }
}
