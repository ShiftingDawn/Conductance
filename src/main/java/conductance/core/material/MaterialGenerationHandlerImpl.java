package conductance.core.material;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.resources.ResourceLocation;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;

@RequiredArgsConstructor
final class MaterialGenerationHandlerImpl implements MaterialGenerationHandler {

	private final Function<Material, String> unlocalizedNameFactory;
	private final boolean hasItem;
	private final boolean autoGenerateItem;
	private final boolean hasBlock;
	private final boolean autoGenerateBlock;
	private final boolean shouldOccludeBlocks;
	private final boolean hasFluid;
	private final boolean autoGenerateFluid;
	private final long unitValue;
	private final Predicate<Material> predicate;
	private final @Nullable ResourceLocation textureType;

	@Override
	public boolean hasItem() {
		return this.hasItem;
	}

	@Override
	public boolean autoGenerateItem() {
		return this.autoGenerateItem;
	}

	@Override
	public boolean hasBlock() {
		return this.hasBlock;
	}

	@Override
	public boolean autoGenerateBlock() {
		return this.autoGenerateBlock;
	}

	@Override
	public boolean shouldOccludeBlocks() {
		return this.shouldOccludeBlocks;
	}

	@Override
	public boolean hasFluid() {
		return this.hasFluid;
	}

	@Override
	public boolean autoGenerateFluid() {
		return this.autoGenerateFluid;
	}

	@Override
	public long getUnitValue() {
		return this.unitValue;
	}

	@Override
	public String getUnlocalizedName(final Material material) {
		return this.unlocalizedNameFactory.apply(material);
	}

	@Override
	public boolean test(final Material material) {
		return this.predicate.test(material);
	}

	@Override
	public ResourceLocation getTextureType() {
		if (this.textureType == null) {
			return Objects.requireNonNull(CAPI.regs().materialGenerationHandlers().getKey(this), "Unregistered material generation handler");
		}
		return this.textureType;
	}
}
