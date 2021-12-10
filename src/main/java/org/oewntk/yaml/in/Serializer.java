/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.yaml.in;

import java.io.File;
import java.io.IOException;

import org.oewntk.model.Model;
import org.oewntk.model.Serialize;

public class Serializer
{
	static public void main(String[] args) throws IOException
	{
		File file = new File(args[2]);

		final Model model = Factory.makeModel(args);
		Serialize.serializeModel(model, file);
	}
}
