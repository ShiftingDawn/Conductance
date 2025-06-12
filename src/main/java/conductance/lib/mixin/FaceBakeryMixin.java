package conductance.lib.mixin;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockElementRotation;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.client.model.QuadTransformers;
import com.llamalad7.mixinextras.sugar.Local;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FaceBakery.class)
public abstract class FaceBakeryMixin {

	@Inject(method = "bakeQuad", at = @At(value = "RETURN"))
	private void conductance$bakeQuadEmissive(
			final Vector3f posFrom, final Vector3f posTo, final BlockElementFace face, final TextureAtlasSprite sprite, final Direction facing, final ModelState transform,
			final BlockElementRotation rotation, final boolean shade, final CallbackInfoReturnable<BakedQuad> cir, @Local final BakedQuad quad
	) {
		if (face.tintIndex() <= -100) {
			QuadTransformers.settingEmissivity(15).processInPlace(quad);
		}
	}
}
