package conductance.init.fluid;

import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import lombok.Getter;
import conductance.api.CAPI;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;
import conductance.runtimepack.client.ResourceHelper;

@SuppressWarnings("removal")
public final class MaterialFluidType extends FluidType {

	@Getter
	private final Material material;
	private final TaggedMaterialSet set;

	public MaterialFluidType(final Material material, final TaggedMaterialSet set, final Properties properties) {
		super(properties);
		this.material = material;
		this.set = set;
	}

	@Override
	public Component getDescription(final FluidStack stack) {
		return CAPI.translations().makeLocalizedName(this);
	}

	@Override
	public Component getDescription() {
		return CAPI.translations().makeLocalizedName(this);
	}

	@Override
	public void initializeClient(final Consumer<IClientFluidTypeExtensions> consumer) {
		consumer.accept(new IClientFluidTypeExtensions() {

			@Override
			public int getTintColor() {
				return MaterialFluidType.this.material.getMaterialColorARGB();
			}

			@Override
			public ResourceLocation getStillTexture() {
				final ResourceLocation texture = ResourceHelper.getCustomMaterialTexture(MaterialFluidType.this.material, MaterialFluidType.this.set.getTextureType());
				if (texture != null) {
					return texture;
				}
				return CAPI.resourceFinder().getTexture(MaterialFluidType.this.material.getTextureSet(), MaterialFluidType.this.set.getTextureType(), null, null).getValue();
			}

			@Override
			public ResourceLocation getFlowingTexture() {
				return this.getFlowingTexture();
			}
		});
	}
}
