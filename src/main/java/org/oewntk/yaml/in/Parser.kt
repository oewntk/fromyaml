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
import java.util.Collection;

/**
 * YAML parser that supplies model
 */
public class Parser
{
	private final File inDir;

	/**
	 * Constructor
	 *
	 * @param inDir dir containing YAML files
	 */
	public Parser(final File inDir)
	{
		this.inDir = inDir;
	}

	/**
	 * Parse
	 *
	 * @return core model
	 * @throws IOException io exception
	 */
	public CoreModel parse() throws IOException
	{
		// lexes + senses
		LexParser lexParser = new LexParser(inDir);
		Collection<Lex> lexes = lexParser.parse();
		Collection<Sense> senses = lexParser.getSenses();

		// synsets
		SynsetParser synsetParser = new SynsetParser(inDir);
		Collection<Synset> synsets = synsetParser.parse();

		return new CoreModel(lexes, senses, synsets);
	}
}
