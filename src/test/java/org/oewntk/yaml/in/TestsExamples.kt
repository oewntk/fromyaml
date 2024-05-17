/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.junit.BeforeClass
import org.junit.Test
import java.io.PrintStream

class TestsExamples {

    val ps: PrintStream = if (!System.getProperties().containsKey("SILENT")) Tracing.psInfo else Tracing.psNull

    private val enclosed = "\"Israel has been the fastest to roll out inoculations.\""
    private val even = "Examples of front vowels include \"a\" in \"man\" and \"e\" in \"gel\""
    private val uneven = "Examples of front vowels include \"a\" in \"man\" and \"e in \"gel\""
    private val spaces = "  Examples of front vowels include \"a\" in \"man\" and \"e in \"gel\"    "

    private fun process(example: String): String {
        ps.println("<$example>")
        val processed = SynsetParser.processExample(example)
        ps.println("<$processed>")
        return processed
    }

    @Test
    fun testEnclosingQuotes() {
        process(enclosed)
    }

    @Test
    fun testEvenQuotes() {
        process(even)
    }

    @Test
    fun testUnEvenQuotes() {
        process(uneven)
    }

    @Test
    fun testSpaces() {
        process(spaces)
    }

    companion object {

        @JvmStatic
        @BeforeClass
        fun init() {
        }
    }
}
