package conductance.api.resource;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public interface BlockStateModelDefiner {

	BlockStateModelPropsBuilder model(ResourceLocation modelLocation);

	default BlockStateModelPropsBuilder model(final Block referenceBlock) {
		return this.model(BuiltInRegistries.BLOCK.getKey(referenceBlock).withPrefix("block/"));
	}
}
