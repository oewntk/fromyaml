/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.yaml.in;

import org.oewntk.model.Synset;

import java.io.File;
import java.util.*;

public class SynsetParser extends YamProcessor1<Synset, String, Map<String, Object>>
{
	private static final boolean DUMP = false;

	private static final String KEY_SYNSET_POS = "partOfSpeech";
	private static final String KEY_SYNSET_DEFINITION = "definition";
	private static final String KEY_SYNSET_EXAMPLE = "example";
	private static final String KEY_SYNSET_MEMBERS = "members";
	private static final String KEY_SYNSET_ILI = "ili";
	private static final String KEY_SYNSET_WIKIDATA = "wikidata";
	private static final String KEY_SYNSET_SOURCE = "source";

	private static final String KEY_EXAMPLE_SOURCE = "source";
	private static final String KEY_EXAMPLE_TEXT = "text";

	private static final String[] VALID_SYNSET_RELATIONS = new String[]{ //
			"hypernym", // "hyponym",
			"instance_hypernym", // "instance_hyponym",
			"mero_part", // "holo_part",
			"mero_member", // "holo_member",
			"mero_substance", // "holo_substance",
			"causes", // "is_caused_by",
			"entails", // "is_entailed_by",
			"exemplifies", // "is_exemplified_by",
			"domain_topic", // "has_domain_topic"
			"domain_region", // "has_domain_region"
			"attribute", //
			"similar", //
			"also", //
	};

	private static final String[] SYNSET_RELATIONS = new String[]{ //
			"hypernym", "hyponym", //
			"instance_hypernym", "instance_hyponym", //
			"mero_part", "holo_part", //
			"mero_member", "holo_member", //
			"mero_substance", "holo_substance", //
			"causes", "is_caused_by", //
			"entails", "is_entailed_by", //
			"exemplifies", "is_exemplified_by", //
			"domain_topic", "has_domain_topic", //
			"domain_region", "has_domain_region", //
			"attribute", //
			"similar", //
			"also", //
	};

	/*
	ignored
		HOLONYM,
		HOLO_LOCATION,
		HOLO_PORTION,
		INVOLVED,
		STATE_OF,
		IS_SUBEVENT_OF,
		IN_MANNER,
		RESTRICTED_BY,
		CLASSIFIED_BY,
		INVOLVED_AGENT,
		INVOLVED_PATIENT,
		INVOLVED_RESULT,
		INVOLVED_INSTRUMENT,
		INVOLVED_LOCATION,
		INVOLVED_DIRECTION,
		INVOLVED_TARGET_DIRECTION,
		INVOLVED_SOURCE_DIRECTION,
		CO_PATIENT_AGENT,
		CO_INSTRUMENT_AGENT,
		CO_RESULT_AGENT,
		CO_INSTRUMENT_PATIENT,
		CO_INSTRUMENT_RESULT]
	*/

	private static final String[] VOID_STRING_ARRAY = new String[0];

	public SynsetParser(final File dir)
	{
		super(dir);
		this.dir = dir;
	}

	@Override
	protected File[] getFiles()
	{
		return dir.listFiles((f) -> f.getName().matches("(noun|verb|adj|adv).*\\.yaml"));
	}

	@Override
	protected Synset processEntry(String source, Map.Entry<String, Map<String, Object>> entry)
	{
		String domain = source.split("\\.")[1];
		String id = entry.getKey();
		Map<String, Object> synsetMap = entry.getValue();
		if (DUMP)
		{
			Tracing.psInfo.println(id);
			YamlUtils.dumpMap("%s %s%n", synsetMap);
		}
		YamlUtils.assertKeysIn(source, synsetMap.keySet(), VALID_SYNSET_RELATIONS, KEY_SYNSET_POS, KEY_SYNSET_DEFINITION, KEY_SYNSET_EXAMPLE, KEY_SYNSET_MEMBERS, KEY_SYNSET_WIKIDATA, KEY_SYNSET_ILI, KEY_SYNSET_SOURCE);

		String code = (String) synsetMap.get(KEY_SYNSET_POS);
		List<String> definitions = (List<String>) synsetMap.get(KEY_SYNSET_DEFINITION);
		List<Object> examples = (List<Object>) synsetMap.get(KEY_SYNSET_EXAMPLE);
		List<String> members = (List<String>) synsetMap.get(KEY_SYNSET_MEMBERS);
		String wikidata = (String) synsetMap.get(KEY_SYNSET_WIKIDATA);

		// relations
		Map<String, Set<String>> relations = null;
		for (String relationKey : SYNSET_RELATIONS)
		{
			if (synsetMap.containsKey(relationKey))
			{
				List<String> relationTargets = (List<String>) synsetMap.get(relationKey);
				assert relationTargets != null;
				if (relations == null)
				{
					relations = new TreeMap<>();
				}
				relations.computeIfAbsent(relationKey, (k) -> new LinkedHashSet<>()).addAll(relationTargets);
			}
		}

		return new Synset(id, code.charAt(0), domain, //
				members == null ? null : members.toArray(VOID_STRING_ARRAY), //
				definitions == null ? null : definitions.toArray(VOID_STRING_ARRAY),  //
				examples == null ? null : examplesToArray(source, examples),  //
				wikidata, relations);
	}

	static public String[] examplesToArray(String source, List<Object> examples)
	{
		int n = examples.size();
		String[] array = new String[n];
		int i = 0;
		for (Object example : examples)
		{
			if (example instanceof String)
			{
				array[i] = (String) example;
			}
			else if (example instanceof Map)
			{
				Map<String, Object> exampleMap = (Map<String, Object>) example;
				YamlUtils.assertKeysIn(source, exampleMap.keySet(), KEY_EXAMPLE_SOURCE, KEY_EXAMPLE_TEXT);
				array[i] = ((Map<String, Object>) example).get(KEY_EXAMPLE_TEXT).toString();
			}
			i++;
		}
		return array;
	}
}
