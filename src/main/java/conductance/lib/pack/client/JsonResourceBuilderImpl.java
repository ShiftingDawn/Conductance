package conductance.lib.pack.client;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.Util;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import conductance.api.resource.JsonResourceBuilder;

@SuppressWarnings("unchecked")
abstract class JsonResourceBuilderImpl<BUILDER extends JsonResourceBuilder<BUILDER>> implements JsonResourceBuilder<BUILDER> {

	private final Map<String, JsonElement> customJsonData = new HashMap<>();

	protected abstract void populateJson(JsonObject json);

	@Override
	public final BUILDER addProperty(final String propertyKey, final String propertyValue) {
		this.customJsonData.put(propertyKey, new JsonPrimitive(propertyValue));
		return (BUILDER) this;
	}

	@Override
	public final BUILDER addProperty(final String propertyKey, final boolean propertyValue) {
		this.customJsonData.put(propertyKey, new JsonPrimitive(propertyValue));
		return (BUILDER) this;
	}

	@Override
	public final BUILDER addProperty(final String propertyKey, final Number propertyValue) {
		this.customJsonData.put(propertyKey, new JsonPrimitive(propertyValue));
		return (BUILDER) this;
	}

	@Override
	public final BUILDER addProperty(final String propertyKey, final char propertyValue) {
		this.customJsonData.put(propertyKey, new JsonPrimitive(propertyValue));
		return (BUILDER) this;
	}

	@Override
	public BUILDER addProperty(final String propertyKey, final JsonElement propertyValue) {
		this.customJsonData.put(propertyKey, propertyValue);
		return (BUILDER) this;
	}

	protected final JsonObject build() {
		return Util.make(new JsonObject(), json -> {
			this.populateJson(json);
			this.customJsonData.forEach(json::add);
		});
	}
}
