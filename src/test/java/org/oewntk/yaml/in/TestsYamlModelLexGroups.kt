/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.model.LibTestModelLexGroups.testCICounts
import org.oewntk.model.LibTestModelLexGroups.testCICountsFromMap
import org.oewntk.model.LibTestModelLexGroups.testCIHypermap3
import org.oewntk.model.LibTestModelLexGroups.testCILemmas
import org.oewntk.model.LibTestModelLexGroups.testCILexesFor
import org.oewntk.model.LibTestModelLexGroups.testCILexesFor3
import org.oewntk.model.LibTestModelLexGroups.testCIMultipleAll
import org.oewntk.yaml.`in`.LibTestsYamlCommon.model
import org.oewntk.yaml.`in`.LibTestsYamlCommon.ps

class TestsYamlModelLexGroups {

	@Test
	fun testCIMultipleAll() {
		testCIMultipleAll(model!!, ps)
	}

	@Test
	fun testCILemmas() {
		testCILemmas(model!!, "battle of verdun", ps)
	}

	@Test
	fun testCICounts() {
		testCICounts(model!!, "battle of verdun", ps)
	}

	@Test
	fun testCICountsFromMap() {
		testCICountsFromMap(model!!, "battle of verdun", ps)
	}

	@Test
	fun testCIHypermapWest() {
		testCIHypermap3(model!!, "West", ps)
	}

	@Test
	fun testCIHypermapBaroque() {
		testCIHypermap3(model!!, "Baroque", ps)
	}

	@Test
	fun testCIAi() {
		testCILexesFor(model!!, "ai", ps)
	}

	@Test
	fun testCIBaroque() {
		testCILexesFor(model!!, "baroque", ps)
	}

	@Test
	fun testCIWest3() {
		testCILexesFor3(model!!, "West", ps)
	}

	@Test
	fun testCIBaroque3() {
		testCILexesFor3(model!!, "Baroque", ps)
	}

	@Test
	fun testCIAi3() {
		testCILexesFor3(model!!, "Ai", ps)
	}

	@Test
	fun testCIAbsolute3() {
		testCILexesFor3(model!!, "Absolute", ps)
	}

	companion object {

		@JvmStatic
		@BeforeClass
		fun init() {
			LibTestsYamlCommon.init()
			checkNotNull(model)
		}
	}
}
