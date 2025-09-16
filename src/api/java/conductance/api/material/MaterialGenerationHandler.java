package conductance.api.material;

import net.minecraft.resources.ResourceLocation;

public interface MaterialGenerationHandler {

	boolean hasItem();

	boolean autoGenerateItem();

	boolean hasBlock();

	boolean autoGenerateBlock();

	boolean shouldOccludeBlocks();

	boolean hasFluid();

	boolean autoGenerateFluid();

	long getUnitValue();

	String getUnlocalizedName(Material material);

	boolean test(Material material);

	ResourceLocation getTextureType();
}
