package conductance.content.fluid;

import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;

@SuppressWarnings("removal")
public class MaterialFluidType extends FluidType {

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
			public ResourceLocation getStillTexture() {
				return MaterialFluidType.this.set.getTextureType().getFluidTexture(MaterialFluidType.this.material.getTextureSet()).getValue();
			}

			@Override
			public ResourceLocation getFlowingTexture() {
				return this.getFlowingTexture();
			}
		});
	}
}
