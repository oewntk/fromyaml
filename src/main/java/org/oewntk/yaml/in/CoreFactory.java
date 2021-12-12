/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.yaml.in;

import org.oewntk.model.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class CoreFactory implements Supplier<CoreModel>
{
	private final File inDir;

	public CoreFactory(final File inDir)
	{
		this.inDir = inDir;
	}

	@Override
	public CoreModel get()
	{
		try
		{
			// lexes + senses
			LexProcessor lexProcessor = new LexProcessor(inDir);
			Map<String, List<Lex>> lexesByLemma = lexProcessor.process();
			Map<String, Sense> sensesById = lexProcessor.getSensesById();

			// synsets
			SynsetProcessor synsetProcessor = new SynsetProcessor(inDir);
			Map<String, Synset> synsetsById = null;
			synsetsById = synsetProcessor.process();
			InverseRelationFactory.make(synsetsById);

			return new CoreModel(lexesByLemma, sensesById, synsetsById).setSource(inDir);
		}
		catch (IOException ioe)
		{
			System.err.println(ioe.getMessage());
			return null;
		}
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
		CoreModel model = new CoreFactory(inDir).get();

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
		CoreModel model = makeCoreModel(args);
		System.err.printf("model %s\n%s\n%s%n", model.getSource(), model.info(), model.counts());
	}
}