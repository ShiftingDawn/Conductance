package conductance.content.fluid;

import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;
import lombok.Getter;
import conductance.api.CAPI;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;
import conductance.client.resourcepack.ResourceHelper;

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
	public void initializeClient(final Consumer<IClientFluidTypeExtensions> consumer) {
		consumer.accept(new IClientFluidTypeExtensions() {

			@Override
			public int getTintColor() {
				return MaterialFluidType.this.material.getMaterialColorARGB();
			}

			@Override
			public ResourceLocation getStillTexture() {
				final ResourceLocation texture = ResourceHelper.getCustomFluidTexture(MaterialFluidType.this.material, MaterialFluidType.this.set.getTextureType());
				if (texture != null) {
					return texture;
				}
				return CAPI.resourceFinder().getFluidTexture(MaterialFluidType.this.material.getTextureSet(), MaterialFluidType.this.set.getTextureType(), null, null).getValue();
			}

			@Override
			public ResourceLocation getFlowingTexture() {
				return this.getFlowingTexture();
			}
		});
	}
}
