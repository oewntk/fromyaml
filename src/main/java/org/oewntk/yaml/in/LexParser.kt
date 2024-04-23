/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */
package org.oewntk.yaml.`in`

import org.oewntk.model.*
import org.oewntk.yaml.`in`.YamlUtils.assertKeysIn
import org.oewntk.yaml.`in`.YamlUtils.dumpMap
import java.io.File
import java.util.*

/**
 * Lex YAML parser
 *
 * @property dir dir containing YAML files
 */
class LexParser(dir: File) : YamProcessor<Lex, String, Map<String, *>>(dir) {

	/**
	 * Accumulated senses as lexes are processed
	 */
	val senses: MutableList<Sense> = ArrayList()

	override val files: Array<File>
		get() = dir.listFiles { f: File -> f.name.matches("entries.*\\.yaml".toRegex()) }!!

	override fun processEntry(source: String?, entry: Pair<String, Map<String, *>>): Collection<Lex> {
		val lexes: MutableList<Lex> = ArrayList<Lex>()

		val lemma = entry.first
		val lemmaMap = entry.second
		for ((type, value1) in lemmaMap) {
			val lexMap = value1 as Map<String, *>
			assertKeysIn(source!!, lexMap.keys, KEY_LEX_SENSE, KEY_LEX_PRONUNCIATION, KEY_LEX_FORM)
			if (DUMP) {
				dumpMap("%s %s%n", lexMap)
			}

			// lex
			val lex = Lex(lemma, type, source)

			// pronunciations
			val pronunciationList = lexMap[KEY_LEX_PRONUNCIATION] as List<Map<String, *>>?
			var pronunciations: Array<Pronunciation>? = null
			if (pronunciationList != null) {
				pronunciations = Array(pronunciationList.size) {
					val pronunciationMap = pronunciationList[it]
					assertKeysIn(source, pronunciationMap.keys, KEY_PRONUNCIATION_VARIETY, KEY_PRONUNCIATION_VALUE)
					if (DUMP) {
						dumpMap("\t%s %s%n", pronunciationMap)
					}
					val variety = pronunciationMap[KEY_PRONUNCIATION_VARIETY] as String?
					val value = pronunciationMap[KEY_PRONUNCIATION_VALUE] as String
					Pronunciation(value, variety)
				}
			}
			lex.pronunciations = pronunciations

			// forms
			val forms = lexMap[KEY_LEX_FORM] as List<String>?
			if (forms != null) {
				lex.forms = forms.toTypedArray()
			}

			// senses
			val senseMaps = lexMap[KEY_LEX_SENSE] as List<Map<String, *>>?
			val lexSenses = MutableList(senseMaps!!.size) {
				val senseMap = senseMaps[it]
				assertKeysIn(
					source, senseMap.keys,
					VALID_SENSE_RELATIONS,  //
					KEY_SENSE_ID,  //
					KEY_SENSE_SYNSET,  //
					KEY_SENSE_VERBFRAMES,  //
					KEY_SENSE_VERBFRAMES,  //
					KEY_SENSE_ADJPOSITION,
					KEY_SENSE_EXAMPLES //
				)
				if (DUMP) {
					dumpMap("\t%s %s%n", senseMap)
				}

				val senseId = senseMap[KEY_SENSE_ID]!! as SenseKey
				val synsetId = senseMap[KEY_SENSE_SYNSET]!! as SynsetId
				val examplesList = senseMap[KEY_SENSE_EXAMPLES] as List<String>?
				val verbFramesList = senseMap[KEY_SENSE_VERBFRAMES] as List<String>?
				val examples = examplesList?.toTypedArray()
				val verbFrames = verbFramesList?.toTypedArray()
				val adjPosition = senseMap[KEY_SENSE_ADJPOSITION] as String?

				// relations
				var relations: MutableMap<String, MutableSet<String>>? = null
				for (relationKey in SENSE_RELATIONS) {
					if (senseMap.containsKey(relationKey)) {
						val relationTargets = senseMap[relationKey] as List<String>?
						if (relations == null) {
							relations = TreeMap()
						}
						relations.computeIfAbsent(relationKey) { LinkedHashSet() }.addAll(relationTargets!!)
					}
				}

				// sense
				val lexSense = Sense(senseId, lex, type[0], it, synsetId, examples, verbFrames, adjPosition, relations)
				senses.add(lexSense)
				lexSense
			}
			lex.senses = lexSenses

			// accumulate
			lexes.add(lex)
		}
		return lexes
	}

	companion object {
		private const val DUMP = false

		private const val KEY_LEX_SENSE = "sense"
		private const val KEY_LEX_PRONUNCIATION = "pronunciation"
		private const val KEY_LEX_FORM = "form"

		private const val KEY_SENSE_ID = "id"
		private const val KEY_SENSE_SYNSET = "synset"
		private const val KEY_SENSE_ADJPOSITION = "adjposition"
		private const val KEY_SENSE_VERBFRAMES = "subcat"
		private const val KEY_SENSE_EXAMPLES = "sent"

		private const val KEY_PRONUNCIATION_VARIETY = "variety"
		private const val KEY_PRONUNCIATION_VALUE = "value"

		private val VALID_SENSE_RELATIONS = arrayOf( //
			"antonym",  //
			"similar",  //
			"exemplifies",  //
			"derivation",  //
			"pertainym",  //
			"participle",  //
			"also",  //
			"domain_region",  //
			"domain_topic",  //

			"state",
			"result",
			"event",
			"property",
			"location",
			"destination",
			"agent",
			"undergoer",
			"uses",
			"instrument",
			"by_means_of",
			"material",
			"vehicle",
			"body_part",

			"other"
		)

		private val SENSE_RELATIONS = arrayOf( //
			"antonym",  //
			"similar",  //
			"exemplifies", "is_exemplified_by",  //
			"derivation",  //
			"pertainym",  //
			"participle",  //
			"also",  //
			"domain_region", "has_domain_region",  //
			"domain_topic", "has_domain_topic",  //

			"agent",
			"material",
			"event",
			"instrument",
			"location",
			"by_means_of",
			"undergoer",
			"property",
			"result",
			"state",
			"uses",
			"destination",
			"body_part",
			"vehicle",

			"other"
		)

		/*
    ignored
      // antonym|
      // also|
      // participle|
      // pertainym|
      // derivation|
      // domain_topic|
      // has_domain_topic|
      // domain_region|
      // has_domain_region|
      // exemplifies|
      // is_exemplified_by|
      // similar|
      // other|
      simple_aspect_ip|
      secondary_aspect_ip|
      simple_aspect_pi|
      secondary_aspect_pi|
      feminine|
      has_feminine|
      masculine|
      has_masculine|
      young|
      has_young|
      diminutive|
      has_diminutive|
      augmentative|
      has_augmentative|
      anto_gradable|
      anto_simple|
      anto_converse
     */
	}
}
