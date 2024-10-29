package conductance.api.resource;

import net.minecraft.resources.ResourceLocation;

@FunctionalInterface
public interface BlockStateModelDefiner {

	BlockStateModelPropsBuilder model(ResourceLocation modelLocation);
}
