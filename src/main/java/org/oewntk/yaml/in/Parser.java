/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.yaml.in;

import org.oewntk.model.CoreModel;
import org.oewntk.model.Lex;
import org.oewntk.model.Sense;
import org.oewntk.model.Synset;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Parser
{
	private final File inDir;

	public Parser(File inDir)
	{
		this.inDir = inDir;
	}

	public CoreModel parse() throws IOException
	{
		// lexes + senses
		LexParser lexParser = new LexParser(inDir);
		Map<String, List<Lex>> lexesByLemma = lexParser.parse();
		Map<String, Sense> sensesById = lexParser.getSensesById();

		// synsets
		SynsetParser synsetParser = new SynsetParser(inDir);
		Map<String, Synset> synsetsById = synsetParser.parse();

		return new CoreModel(lexesByLemma, sensesById, synsetsById);
	}
}
