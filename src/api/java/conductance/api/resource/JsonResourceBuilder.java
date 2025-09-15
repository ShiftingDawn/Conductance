package conductance.api.resource;

import net.minecraft.resources.ResourceLocation;

public interface JsonResourceBuilder<BUILDER extends JsonResourceBuilder<BUILDER>> {

	BUILDER addProperty(String propertyKey, String propertyValue);

	BUILDER addProperty(String propertyKey, boolean propertyValue);

	BUILDER addProperty(String propertyKey, Number propertyValue);

	BUILDER addProperty(String propertyKey, char propertyValue);

	default BUILDER addProperty(final String propertyKey, final ResourceLocation propertyValue) {
		return this.addProperty(propertyKey, propertyValue.toString());
	}
}
