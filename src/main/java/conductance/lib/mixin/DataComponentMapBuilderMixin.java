package conductance.lib.mixin;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import conductance.lib.mixinext.ItemPropertiesBuilderExtension;

@Mixin(DataComponentMap.Builder.class)
public abstract class DataComponentMapBuilderMixin implements ItemPropertiesBuilderExtension {

	@Shadow
	@Final
	private Reference2ObjectMap<DataComponentType<?>, Object> map;

	@Override
	public boolean conductance$has(final DataComponentType<?> componentType) {
		return this.map.containsKey(componentType);
	}
}
