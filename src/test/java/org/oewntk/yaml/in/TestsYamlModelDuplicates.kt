/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.model.LibTestModelDuplicates.testDuplicatesForKeyIC
import org.oewntk.model.LibTestModelDuplicates.testDuplicatesForKeyOEWN
import org.oewntk.model.LibTestModelDuplicates.testDuplicatesForKeyPWN
import org.oewntk.model.LibTestModelDuplicates.testDuplicatesForKeyPos
import org.oewntk.yaml.`in`.LibTestsYamlCommon.model

class TestsYamlModelDuplicates {
	@Test
	fun testKeyOEWN() {
		testDuplicatesForKeyOEWN(LibTestsYamlCommon.model!!, LibTestsYamlCommon.ps)
	}

	@Test
	fun testKeyPos() {
		testDuplicatesForKeyPos(LibTestsYamlCommon.model!!, LibTestsYamlCommon.ps)
	}

	@Test
	fun testKeyIC() {
		testDuplicatesForKeyIC(LibTestsYamlCommon.model!!, LibTestsYamlCommon.ps)
	}

	@Test
	fun testKeyPWN() {
		testDuplicatesForKeyPWN(LibTestsYamlCommon.model!!, LibTestsYamlCommon.ps)
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
