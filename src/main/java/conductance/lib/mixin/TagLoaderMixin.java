package conductance.lib.mixin;

import java.util.List;
import java.util.Map;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagLoader;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import conductance.lib.mixinext.MixinTagLoaderExtension;
import conductance.core.runtimepack.server.RuntimeDataPackBridge;

@Mixin(TagLoader.class)
public abstract class TagLoaderMixin<T> implements MixinTagLoaderExtension<T> {

	@Unique
	@Nullable
	private Registry<T> conductance$registry;

	@Inject(method = "load", at = @At("RETURN"))
	public void conductance$injectTags(final ResourceManager resourceManager, final CallbackInfoReturnable<Map<ResourceLocation, List<TagLoader.EntryWithSource>>> cir) {
		final Registry<T> reg = this.conductance$getRegistry();
		if (reg == null) {
			return;
		}
		RuntimeDataPackBridge.generateTags(reg, cir.getReturnValue());
	}

	@Override
	public void conductance$setRegistry(final Registry<T> registry) {
		this.conductance$registry = registry;
	}

	@Nullable
	@Override
	public Registry<T> conductance$getRegistry() {
		return this.conductance$registry;
	}
}
