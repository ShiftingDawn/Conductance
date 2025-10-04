package conductance.init.fluid;

import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import lombok.RequiredArgsConstructor;
import conductance.api.CAPI;

@RequiredArgsConstructor
public final class MaterialClientFluidTypeExtensions implements IClientFluidTypeExtensions {

	private final MaterialFluidType fluidType;

	@Override
	public int getTintColor(final FluidStack stack) {
		return this.fluidType.getMaterial().getColor().getCurrentColor();
	}

	@Override
	public ResourceLocation getStillTexture() {
		return Objects.requireNonNullElseGet(
				CAPI.resourceFinder().getCustomMaterialTexture(this.fluidType.getMaterial(), this.fluidType.getHandler().getTextureType()),
				() -> CAPI.resourceFinder().getMaterialTexture(this.fluidType.getMaterial().getTextureSet(), this.fluidType.getHandler().getTextureType(), null, null).value()
		);
	}

	@Override
	public ResourceLocation getFlowingTexture() {
		return this.getStillTexture();
	}
}
