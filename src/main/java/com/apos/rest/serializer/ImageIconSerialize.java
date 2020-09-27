package com.apos.rest.serializer;

import java.io.IOException;

import javax.swing.ImageIcon;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ImageIconSerialize extends JsonSerializer<ImageIcon>{

	@Override
	public void serialize(ImageIcon value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

		gen.writeStartObject();
		gen.writeObjectField("icon", value.toString());
		gen.writeEndObject();
	}

}
