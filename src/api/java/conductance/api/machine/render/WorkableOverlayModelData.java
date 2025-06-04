package conductance.api.machine.render;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import com.lowdragmc.lowdraglib.client.bakedpipeline.FaceQuad;
import com.lowdragmc.lowdraglib.client.model.ModelFactory;
import com.lowdragmc.lowdraglib.utils.ResourceHelper;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.util.world.RelativeDirection;

public final class WorkableOverlayModelData {

	private final ResourceLocation modelLocation;
	@OnlyIn(Dist.CLIENT)
	private Map<RelativeDirection, FaceTexture> textureData;
	@OnlyIn(Dist.CLIENT)
	private Table<Direction, Direction, List<BakedQuad>[][]> textureCache;

	public WorkableOverlayModelData(final ResourceLocation modelLocation) {
		this.modelLocation = modelLocation;
		if (CAPI.isClient()) {
			this.textureData = new EnumMap<>(RelativeDirection.class);
			this.textureCache = Tables.newCustomTable(new EnumMap<>(Direction.class), () -> new EnumMap<>(Direction.class));
		}
	}

	@SuppressWarnings("unchecked")
	@OnlyIn(Dist.CLIENT)
	public List<BakedQuad> getQuads(@Nullable final Direction side, final Direction frontSide, final boolean isWorking, final boolean canWork) {
		if (side == null) {
			return Collections.emptyList();
		}
		List<BakedQuad>[][] cache = this.textureCache.get(side, frontSide);
		if (cache == null) {
			cache = new List[2][2];
			this.textureCache.put(side, frontSide, cache);
		}
		if (cache[isWorking ? 1 : 0][canWork ? 1 : 0] == null) {
			final List<BakedQuad> quads = new ArrayList<>();
			for (final Direction quadSide : Direction.values()) {
				final ModelState rotation = ModelFactory.getRotation(frontSide);
				final FaceTexture faceData = this.textureData.get(RelativeDirection.byDirection(quadSide));
				if (faceData != null) {
					TextureAtlasSprite texture = faceData.getSprite(isWorking, canWork);
					if (texture != null) {
						final BakedQuad quad = FaceQuad.bakeFace(FaceQuad.BLOCK, quadSide, texture, rotation, -1, 0, true, true);
						if (quad.getDirection() == side) {
							quads.add(quad);
						}
					}
					texture = faceData.getSpriteEmissive(isWorking, canWork);
					if (texture != null) {
						final BakedQuad quad = FaceQuad.bakeFace(FaceQuad.BLOCK, quadSide, texture, rotation, -101, 15, true, true);
						if (quad.getDirection() == side) {
							quads.add(quad);
						}
					}
				}
			}
			cache[isWorking ? 1 : 0][canWork ? 1 : 0] = quads;
		}
		return cache[isWorking ? 1 : 0][canWork ? 1 : 0];
	}

	@OnlyIn(Dist.CLIENT)
	public void registerTextures(final Consumer<ResourceLocation> register) {
		this.textureData.clear();
		this.textureCache.clear();
		for (final RelativeDirection side : RelativeDirection.values()) {
			final ResourceLocation texDefault = this.modelLocation.withSuffix("/" + side);
			if (!ResourceHelper.isTextureExist(texDefault)) {
				continue;
			}
			register.accept(texDefault);
			ResourceLocation texWorking = texDefault.withSuffix("_working");
			if (ResourceHelper.isTextureExist(texWorking)) {
				register.accept(texWorking);
			} else {
				texWorking = texDefault;
			}
			ResourceLocation texPaused = texDefault.withSuffix("_paused");
			if (ResourceHelper.isTextureExist(texPaused)) {
				register.accept(texPaused);
			} else {
				texPaused = texDefault;
			}
			ResourceLocation texDefaultEmissive = texDefault.withSuffix("_emissive");
			if (ResourceHelper.isTextureExist(texDefaultEmissive)) {
				register.accept(texDefaultEmissive);
			} else {
				texDefaultEmissive = null;
			}
			ResourceLocation texWorkingEmissive = texDefault.withSuffix("_working_emissive");
			if (ResourceHelper.isTextureExist(texWorkingEmissive)) {
				register.accept(texWorkingEmissive);
			} else {
				texWorkingEmissive = null;
			}
			ResourceLocation texPausedEmissive = texDefault.withSuffix("_paused_emissive");
			if (ResourceHelper.isTextureExist(texPausedEmissive)) {
				register.accept(texPausedEmissive);
			} else {
				texPausedEmissive = null;
			}
			this.textureData.put(side, new FaceTexture(texDefault, texPaused, texWorking, texDefaultEmissive, texPausedEmissive, texWorkingEmissive));
		}
	}

	@OnlyIn(Dist.CLIENT)
	private record FaceTexture(
			@Nullable ResourceLocation textureDefault, @Nullable ResourceLocation texturePaused, @Nullable ResourceLocation textureWorking,
			@Nullable ResourceLocation textureDefaultEmissive, @Nullable ResourceLocation texturePausedEmissive, @Nullable ResourceLocation textureWorkingEmissive
	) {
		@Nullable
		public TextureAtlasSprite getSprite(final boolean active, final boolean working) {
			return this.getSprite(active, working, this.textureDefault, this.textureWorking, this.texturePaused);
		}

		@Nullable
		public TextureAtlasSprite getSpriteEmissive(final boolean active, final boolean working) {
			return this.getSprite(active, working, this.textureDefaultEmissive, this.textureWorkingEmissive, this.texturePausedEmissive);
		}

		@Nullable
		public TextureAtlasSprite getSprite(
				final boolean active, final boolean working,
				@Nullable final ResourceLocation texDefault, @Nullable final ResourceLocation texWorking, @Nullable final ResourceLocation texPaused
		) {
			final ResourceLocation tex = active ? working ? texWorking : texPaused : texDefault;
			return tex != null ? ModelFactory.getBlockSprite(tex) : null;
		}
	}
}
