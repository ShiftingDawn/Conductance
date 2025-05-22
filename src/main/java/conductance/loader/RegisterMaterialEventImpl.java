package conductance.loader;

import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import lombok.AllArgsConstructor;
import conductance.api.material.Material;
import conductance.api.plugin.MaterialBuilder;
import conductance.api.plugin.RegisterMaterialEvent;
import conductance.core.apiimpl.MaterialBuilderImpl;

@AllArgsConstructor
final class RegisterMaterialEventImpl implements RegisterMaterialEvent {

	private final String modid;
	private final Function<ResourceLocation, MaterialBuilderImpl> delegate;

	@Override
	public Material register(final String registryName, final Consumer<MaterialBuilder> builder) {
		//TODO refactor
		return Util.make(this.delegate.apply(ResourceLocation.fromNamespaceAndPath(this.modid, registryName)), builder).build();
	}
}
