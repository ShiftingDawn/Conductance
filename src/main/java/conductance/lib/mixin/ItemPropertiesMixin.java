package conductance.lib.mixin;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import conductance.lib.mixinext.ItemPropertiesBuilderExtension;

@Mixin(Item.Properties.class)
public abstract class ItemPropertiesMixin {

	@WrapOperation(method = "buildAndValidateComponents", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/component/DataComponentMap$Builder;set(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Lnet/minecraft/core/component/DataComponentMap$Builder;"))
	private <T> DataComponentMap.Builder conductance$overrideName(final DataComponentMap.Builder instance, final DataComponentType<T> component, final T value, final Operation<DataComponentMap.Builder> original) {
		if (component == DataComponents.ITEM_NAME) {
			if (!((ItemPropertiesBuilderExtension) instance).conductance$has(DataComponents.ITEM_NAME)) {
				instance.set(component, value);
			}
		} else {
			instance.set(component, value);
		}
		return instance;
	}
}
