/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.yaml.in;

import org.oewntk.model.Lex;
import org.oewntk.model.Pronunciation;
import org.oewntk.model.Sense;

import java.io.File;
import java.util.*;
import java.util.Map.Entry;

/**
 * Lex parser
 */
public class LexParser extends YamProcessor<Lex, String, Map<String, Object>>
{
    private static final boolean DUMP = false;

    private static final String KEY_LEX_SENSE = "sense";
    private static final String KEY_LEX_PRONUNCIATION = "pronunciation";
    private static final String KEY_LEX_FORM = "form";

    private static final String KEY_SENSE_ID = "id";
    private static final String KEY_SENSE_SYNSET = "synset";
    private static final String KEY_SENSE_ADJPOSITION = "adjposition";
    private static final String KEY_SENSE_VERBFRAMES = "subcat";
    private static final String KEY_SENSE_EXAMPLES = "sent";

    private static final String KEY_PRONUNCIATION_VARIETY = "variety";
    private static final String KEY_PRONUNCIATION_VALUE = "value";

    private static final String[] VALID_SENSE_RELATIONS = { //
            "antonym", //
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

            "other"};

    private static final String[] SENSE_RELATIONS = { //
            "antonym", //
            "similar",  //
            "exemplifies", "is_exemplified_by", //
            "derivation",  //
            "pertainym",  //
            "participle",  //
            "also",  //
            "domain_region", "has_domain_region", //
            "domain_topic", "has_domain_topic", //

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
    };

    private static final String[] VOID_STRING_ARRAY = new String[0];

    /**
     * Accumulated senses as lexes are processed
     */
    private final List<Sense> senses = new ArrayList<>();

    /**
     * Lex YAML parser
     *
     * @param dir dir containing YAML files
     */
    public LexParser(final File dir)
    {
        super(dir);
    }

    @Override
    protected File[] getFiles()
    {
        return dir.listFiles((f) -> f.getName().matches("entries.*\\.yaml"));
    }

    @Override
    protected Collection<Lex> processEntry(final String source, final Entry<String, Map<String, Object>> lemmaEntry)
    {
        List<Lex> lexes = new ArrayList<>();

        String lemma = lemmaEntry.getKey();
        Map<String, Object> lemmaMap = lemmaEntry.getValue();

        for (Map.Entry<String, Object> typeEntry : lemmaMap.entrySet())
        {
            String type = typeEntry.getKey();
            Map<String, Object> lexMap = (Map<String, Object>) typeEntry.getValue();
            YamlUtils.assertKeysIn(source, lexMap.keySet(), KEY_LEX_SENSE, KEY_LEX_PRONUNCIATION, KEY_LEX_FORM);
            if (DUMP)
            {
                YamlUtils.dumpMap("%s %s%n", lexMap);
            }

            // lex
            Lex lex = new Lex(lemma, type, source);

            // pronunciations
            List<Map<String, Object>> pronunciationList = (List<Map<String, Object>>) lexMap.get(KEY_LEX_PRONUNCIATION);
            Pronunciation[] pronunciations = null;
            if (pronunciationList != null)
            {
                pronunciations = new Pronunciation[pronunciationList.size()];
                int p = 0;
                for (Map<String, Object> pronunciationMap : pronunciationList)
                {
                    YamlUtils.assertKeysIn(source, pronunciationMap.keySet(), KEY_PRONUNCIATION_VARIETY, KEY_PRONUNCIATION_VALUE);
                    if (DUMP)
                    {
                        YamlUtils.dumpMap("\t%s %s%n", pronunciationMap);
                    }
                    String variety = (String) pronunciationMap.get(KEY_PRONUNCIATION_VARIETY);
                    String value = (String) pronunciationMap.get(KEY_PRONUNCIATION_VALUE);
                    pronunciations[p] = new Pronunciation(value, variety);
                    p++;
                }
            }
            lex.setPronunciations(pronunciations);

            // forms
            List<String> forms = (List<String>) lexMap.get(KEY_LEX_FORM);
            if (forms != null)
            {
                lex.setForms(forms.toArray(new String[0]));
            }

            // senses
            List<Map<String, Object>> senseMaps = (List<Map<String, Object>>) lexMap.get(KEY_LEX_SENSE);
            Sense[] lexSenses = new Sense[senseMaps.size()];
            int i = 0;
            for (Map<String, Object> senseMap : senseMaps)
            {
                YamlUtils.assertKeysIn(source, senseMap.keySet(),
                        VALID_SENSE_RELATIONS, //
                        KEY_SENSE_ID, //
                        KEY_SENSE_SYNSET, //
                        KEY_SENSE_VERBFRAMES, //
                        KEY_SENSE_VERBFRAMES, //
                        KEY_SENSE_ADJPOSITION,
                        KEY_SENSE_EXAMPLES //
                );
                if (DUMP)
                {
                    YamlUtils.dumpMap("\t%s %s%n", senseMap);
                }

                String senseId = (String) senseMap.get(KEY_SENSE_ID);
                String synsetId = (String) senseMap.get(KEY_SENSE_SYNSET);
                List<String> examplesList = (List<String>) senseMap.get(KEY_SENSE_EXAMPLES);
                List<String> verbFramesList = (List<String>) senseMap.get(KEY_SENSE_VERBFRAMES);
                String[] examples = examplesList == null ? null : examplesList.toArray(VOID_STRING_ARRAY);
                String[] verbFrames = verbFramesList == null ? null : verbFramesList.toArray(VOID_STRING_ARRAY);
                String adjPosition = (String) senseMap.get(KEY_SENSE_ADJPOSITION);

                // relations
                Map<String, Set<String>> relations = null;
                for (String relationKey : SENSE_RELATIONS)
                {
                    if (senseMap.containsKey(relationKey))
                    {
                        List<String> relationTargets = (List<String>) senseMap.get(relationKey);
                        if (relations == null)
                        {
                            relations = new TreeMap<>();
                        }
                        relations.computeIfAbsent(relationKey, (k) -> new LinkedHashSet<>()).addAll(relationTargets);
                    }
                }

                // sense
                lexSenses[i] = new Sense(senseId, lex, type.charAt(0), i, synsetId, examples, verbFrames, adjPosition, relations);
                senses.add(lexSenses[i]);

                i++;
            }
            lex.setSenses(lexSenses);

            // accumulate
            lexes.add(lex);
        }

        return lexes;
    }

    /**
     * Get senses that was also accumulated
     *
     * @return accumulated senses
     */
    public Collection<Sense> getSenses()
    {
        return this.senses;
    }
}
