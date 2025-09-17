package conductance.lib.mixin;

import java.util.List;
import java.util.Map;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagLoader;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import conductance.lib.pack.server.RuntimeDataPackBridge;

@Mixin(TagLoader.class)
public abstract class TagLoaderMixin {

	@WrapOperation(method = "loadPendingTags", at = @At(value = "INVOKE", target = "Lnet/minecraft/tags/TagLoader;build(Ljava/util/Map;)Ljava/util/Map;"))
	private static <T> Map<ResourceLocation, List<T>> conductance$loadPendingTags(final TagLoader<T> instance, final Map<ResourceLocation, List<TagLoader.EntryWithSource>> builders,
			final Operation<Map<ResourceLocation, List<T>>> original, @Local final ResourceKey<? extends Registry<T>> resourcekey) {
		RuntimeDataPackBridge.generateTags(resourcekey, builders);
		return original.call(instance, builders);
	}
}
