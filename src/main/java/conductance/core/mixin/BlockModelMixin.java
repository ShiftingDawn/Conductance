package conductance.core.mixin;

import java.util.function.Function;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import com.mojang.datafixers.util.Either;
import com.lowdragmc.lowdraglib.client.model.ModelFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import conductance.api.machine.render.TextureOverrider;

@Mixin(BlockModel.class)
public class BlockModelMixin {

	@Unique
	private final ThreadLocal<TextureOverrider> conductance$overrider = ThreadLocal.withInitial(() -> null);
	@Shadow
	public String name;

	@Inject(method = "bake(Lnet/minecraft/client/resources/model/ModelBaker;Lnet/minecraft/client/renderer/block/model/BlockModel;Ljava/util/function/Function;Lnet/minecraft/client/resources/model/ModelState;Z)Lnet/minecraft/client/resources/model/BakedModel;", at = @At(value = "HEAD"))
	private void conductance$retrieveOverrider(final ModelBaker baker, final BlockModel model, final Function<Material, TextureAtlasSprite> spriteGetter, final ModelState state, final boolean guiLight3d,
	                                           final CallbackInfoReturnable<BakedModel> cir) {
		if (spriteGetter instanceof final TextureOverrider textureOverrider) {
			this.conductance$overrider.set(textureOverrider);
		}
	}

	@Inject(method = "bake(Lnet/minecraft/client/resources/model/ModelBaker;Lnet/minecraft/client/renderer/block/model/BlockModel;Ljava/util/function/Function;Lnet/minecraft/client/resources/model/ModelState;Z)Lnet/minecraft/client/resources/model/BakedModel;", at = @At(value = "RETURN"))
	private void conductance$clearOverrider(final ModelBaker baker, final BlockModel model, final Function<Material, TextureAtlasSprite> spriteGetter, final ModelState state, final boolean guiLight3d,
	                                        final CallbackInfoReturnable<BakedModel> cir) {
		if (spriteGetter instanceof TextureOverrider) {
			this.conductance$overrider.remove();
		}
	}

	@Inject(method = "findTextureEntry", at = @At("HEAD"), cancellable = true)
	private void conductance$remapTexture(final String name, final CallbackInfoReturnable<Either<Material, String>> cir) {
		final TextureOverrider overrider = this.conductance$overrider.get();
		if (overrider != null && overrider.overrides().containsKey(name)) {
			final ResourceLocation textureLocation = overrider.overrides().get(name);
			if (textureLocation != null) {
				cir.setReturnValue(ModelFactory.parseBlockTextureLocationOrReference(textureLocation.toString()));
			}
		}
	}
}