/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.yaml.in;

import org.oewntk.model.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Factory
{
	static public CoreModel makeCoreModel(File inDir) throws IOException
	{
		// lexes + senses
		LexProcessor lexProcessor = new LexProcessor(inDir);
		Map<String, List<Lex>> lexesByLemma = lexProcessor.process();
		Map<String, Sense> sensesById = lexProcessor.getSensesById();
		System.err.println("[Model] lexes + senses");

		// synsets
		SynsetProcessor synsetProcessor = new SynsetProcessor(inDir);
		Map<String, Synset> synsetsById = synsetProcessor.process();
		InverseRelationFactory.make(synsetsById);
		System.err.println("[Model] synsets");

		return new CoreModel(lexesByLemma, sensesById, synsetsById).setSource(inDir);
	}

	static public Model makeModel(File inDir, File inDir2) throws IOException
	{
		CoreModel coreModel = makeCoreModel(inDir).setSource(inDir);

		// verb frames and templates
		Map<String, VerbFrame> verbFramesById = new VerbFrameProcessor(inDir).process();
		Map<Integer, VerbTemplate> verbTemplatesById = new VerbTemplateProcessor(inDir2).process();
		Map<String, int[]> senseToVerbTemplates = new SenseToVerbTemplatesProcessor(inDir2).process();
		System.err.println("[Model] verb frames and templates");

		// tag counts
		Map<String, TagCount> senseToTagCounts = new SenseToTagCountsProcessor(inDir2).process();
		System.err.println("[Model] tag counts");

		return new Model(coreModel, verbFramesById, verbTemplatesById, senseToVerbTemplates, senseToTagCounts).setSources(inDir, inDir2);
	}

	static public CoreModel makeCoreModel(String[] args) throws IOException
	{
		// Timing
		final long startTime = System.currentTimeMillis();

		// Heap
		boolean traceHeap = true;
		String traceHeapEnv = System.getenv("TRACEHEAP");
		if (traceHeapEnv != null)
		{
			traceHeap = Boolean.parseBoolean(traceHeapEnv);
		}
		if (traceHeap)
		{
			System.err.println(Memory.heapInfo("before maps,", Memory.Unit.M));
		}

		// Args
		File inDir = new File(args[0]);

		// Make
		CoreModel model = makeCoreModel(inDir);

		// Heap
		if (traceHeap)
		{
			System.gc();
			System.err.println(Memory.heapInfo("after maps,", Memory.Unit.M));
		}

		// Timing
		final long endTime = System.currentTimeMillis();
		System.err.println("[Time] " + (endTime - startTime) / 1000 + "s");

		return model;
	}

	static public Model makeModel(String[] args) throws IOException
	{
		// Timing
		final long startTime = System.currentTimeMillis();

		// Heap
		boolean traceHeap = true;
		String traceHeapEnv = System.getenv("TRACEHEAP");
		if (traceHeapEnv != null)
		{
			traceHeap = Boolean.parseBoolean(traceHeapEnv);
		}
		if (traceHeap)
		{
			System.err.println(Memory.heapInfo("before maps,", Memory.Unit.M));
		}

		// Args
		File inDir = new File(args[0]);
		File inDir2 = new File(args[1]);

		// Make
		Model model = makeModel(inDir, inDir2);

		// Heap
		if (traceHeap)
		{
			System.gc();
			System.err.println(Memory.heapInfo("after maps,", Memory.Unit.M));
		}

		// Timing
		final long endTime = System.currentTimeMillis();
		System.err.println("[Time] " + (endTime - startTime) / 1000 + "s");

		return model;
	}

	static public void main(String[] args) throws IOException
	{
		Model model = makeModel(args);
		System.err.printf("model %s\n%s\n%s%n", Arrays.toString(model.getSources()), model.info(), model.counts());
	}
}