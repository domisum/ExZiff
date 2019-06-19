package de.domisum.exziff.world.transcode;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import de.domisum.exziff.world.block.Block.BlockAttribute;

import java.lang.reflect.Type;

public class BlockAttributeAdapter implements JsonSerializer<BlockAttribute<?>>, JsonDeserializer<BlockAttribute<?>>
{

	@Override
	public JsonElement serialize(BlockAttribute<?> blockAttribute, Type type, JsonSerializationContext jsonSerializationContext)
	{
		JsonObject blockAttributeJsonObject = new JsonObject();
		blockAttributeJsonObject.addProperty("type", blockAttribute.getType().getName());
		blockAttributeJsonObject.add("value", jsonSerializationContext.serialize(blockAttribute.getValue()));

		return blockAttributeJsonObject;
	}

	@Override
	public BlockAttribute<?> deserialize(
			JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException
	{
		String clazzName = jsonElement.getAsJsonObject().get("type").getAsString();
		try
		{
			Class<Object> typeClazz = (Class<Object>) Class.forName(clazzName);
			JsonElement valueJsonElement = jsonElement.getAsJsonObject().get("value");
			Object value = jsonDeserializationContext.deserialize(valueJsonElement, typeClazz);

			return new BlockAttribute<Object>(typeClazz, value);
		}
		catch(ClassNotFoundException e)
		{
			throw new JsonParseException(e);
		}
	}
}
