package conductance.core.pipenet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import com.mojang.blaze3d.vertex.PoseStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import com.lowdragmc.lowdraglib.client.bakedpipeline.FaceQuad;
import com.lowdragmc.lowdraglib.client.model.ModelFactory;
import com.lowdragmc.lowdraglib.client.renderer.IItemRendererProvider;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.render.BakedModelItemDefaults;
import conductance.api.util.SafeOptional;
import conductance.block.PipeBlock;

public class PipeModel {

	private final Map<Optional<Direction>, List<BakedQuad>> itemModelCache = new ConcurrentHashMap<>();
	public final float thickness;
	public final AABB boxCenter;
	public final Map<Direction, AABB> boxSides;

	public Supplier<SafeOptional<ResourceLocation>> textureSide, textureEnd;
	@Nullable
	public Supplier<SafeOptional<ResourceLocation>> textureSideSecondary, textureEndSecondary;
	@Setter
	@Nullable
	public SafeOptional<ResourceLocation> sideOverlayTexture, endOverlayTexture;

	@OnlyIn(Dist.CLIENT)
	@Nullable
	private TextureAtlasSprite spriteSide, spriteEnd, spriteSideSecondary, spriteEndSecondary, spriteSideOverlay, spriteEndOverlay;

	public PipeModel(final float thickness, final Supplier<SafeOptional<ResourceLocation>> textureSide, final Supplier<SafeOptional<ResourceLocation>> textureEnd,
	                 @Nullable final Supplier<SafeOptional<ResourceLocation>> textureSideSecondary, @Nullable final Supplier<SafeOptional<ResourceLocation>> textureEndSecondary) {
		this.textureSide = textureSide;
		this.textureEnd = textureEnd;
		this.textureSideSecondary = textureSideSecondary;
		this.textureEndSecondary = textureEndSecondary;
		this.thickness = thickness;
		final double min = (1d - thickness) / 2;
		final double max = min + thickness;
		this.boxCenter = new AABB(min, min, min, max, max, max);
		this.boxSides = new EnumMap<>(Direction.class);
		for (final Direction side : Direction.values()) {
			final Vec3i normal = side.getNormal();
			this.boxSides.put(side, new AABB(
					normal.getX() == 0 ? min : normal.getX() > 0 ? max : 0,
					normal.getY() == 0 ? min : normal.getY() > 0 ? max : 0,
					normal.getZ() == 0 ? min : normal.getZ() > 0 ? max : 0,
					normal.getX() == 0 ? max : normal.getX() > 0 ? 1 : min,
					normal.getY() == 0 ? max : normal.getY() > 0 ? 1 : min,
					normal.getZ() == 0 ? max : normal.getZ() > 0 ? 1 : min
			));
		}
	}

	public VoxelShape getShapes(final int connections) {
		final List<VoxelShape> shapes = new ArrayList<>(7);
		shapes.add(Shapes.create(this.boxCenter));
		for (final Direction side : Direction.values()) {
			if (this.isConnected(connections, side)) {
				shapes.add(Shapes.create(this.boxSides.get(side)));
			}
		}
		return shapes.stream().reduce(Shapes.empty(), Shapes::or);
	}

	public boolean isConnected(final int connections, final Direction direction) {
		return PipeNetHelper.isConnected(connections, direction);
	}

	@OnlyIn(Dist.CLIENT)
	public List<BakedQuad> bakeQuads(@Nullable final Direction side, final int connections) {
		if (this.spriteSide == null) {
			this.spriteSide = ModelFactory.getBlockSprite(this.textureSide.get().getValue());
		}
		if (this.spriteEnd == null) {
			this.spriteEnd = ModelFactory.getBlockSprite(this.textureEnd.get().getValue());
		}
		if (this.textureSideSecondary != null && !this.textureSideSecondary.get().isFallback() && this.spriteSideSecondary == null) {
			this.spriteSideSecondary = ModelFactory.getBlockSprite(this.textureSideSecondary.get().getValue());
		}
		if (this.textureEndSecondary != null && !this.textureEndSecondary.get().isFallback() && this.spriteEndSecondary == null) {
			this.spriteEndSecondary = ModelFactory.getBlockSprite(this.textureEndSecondary.get().getValue());
		}
		if (this.sideOverlayTexture != null && this.spriteSideOverlay == null) {
			this.spriteSideOverlay = ModelFactory.getBlockSprite(this.sideOverlayTexture.getValue());
		}
		if (this.endOverlayTexture != null && this.spriteEndOverlay == null) {
			this.spriteEndOverlay = ModelFactory.getBlockSprite(this.endOverlayTexture.getValue());
		}
		if (side != null) {
			if (this.thickness == 1) {
				final List<BakedQuad> quads = new ArrayList<>();
				quads.add(FaceQuad.builder(side, this.spriteSide).cube(this.boxCenter).cubeUV().tintIndex(0).bake());
				if (this.spriteSideSecondary != null) {
					quads.add(FaceQuad.builder(side, this.spriteSideSecondary).cube(this.boxCenter).cubeUV().tintIndex(0).bake());
				}
				return quads;
			}
			if (this.isConnected(connections, side)) {
				final List<BakedQuad> quads = new ArrayList<>();
				quads.add(FaceQuad.builder(side, this.spriteEnd).cube(this.boxSides.get(side).inflate(-0.001)).cubeUV().tintIndex(1).bake());
				if (this.spriteEndSecondary != null) {
					quads.add(FaceQuad.builder(side, this.spriteEndSecondary).cube(this.boxSides.get(side)).cubeUV().tintIndex(1).bake());
				}
				if (this.spriteEndOverlay != null) {
					quads.add(FaceQuad.builder(side, this.spriteEndOverlay).cube(this.boxSides.get(side)).cubeUV().tintIndex(0).bake());
				}
				if (this.spriteSideOverlay != null) {
					for (final Direction face : Direction.values()) {
						if (face != side && face != side.getOpposite()) {
							quads.add(FaceQuad.builder(face, this.spriteSideOverlay).cube(this.boxSides.get(side)).cubeUV().tintIndex(2).bake());
						}
					}
				}
				return quads;
			}
			return Collections.emptyList();
		}
		final List<BakedQuad> quads = new LinkedList<>();
		if (this.thickness < 1) {
			for (final Direction face : Direction.values()) {
				if (!this.isConnected(connections, face)) {
					quads.add(FaceQuad.builder(face, this.spriteSide).cube(this.boxCenter).cubeUV().tintIndex(0).bake());
					if (this.spriteSideSecondary != null) {
						quads.add(FaceQuad.builder(face, this.spriteSideSecondary).cube(this.boxCenter).cubeUV().tintIndex(0).bake());
					}
				}
				for (final Direction facing : Direction.values()) {
					if (facing.getAxis() != face.getAxis()) {
						if (this.isConnected(connections, facing)) {
							quads.add(FaceQuad.builder(face, this.spriteSide).cube(this.boxSides.get(facing)).cubeUV().tintIndex(0).bake());
							if (this.spriteSideSecondary != null) {
								quads.add(FaceQuad.builder(face, this.spriteSideSecondary).cube(this.boxSides.get(facing)).cubeUV().tintIndex(0).bake());
							}
							if (this.spriteSideOverlay != null) {
								quads.add(FaceQuad.builder(face, this.spriteSideOverlay).cube(this.boxSides.get(facing).inflate(0.001)).cubeUV().tintIndex(2).bake());
							}
						}
					}
				}
			}
		}
		return quads;
	}

	@OnlyIn(Dist.CLIENT)
	public TextureAtlasSprite getParticleTexture() {
		if (this.spriteSide == null) {
			this.spriteSide = ModelFactory.getBlockSprite(this.textureSide.get().getValue());
		}
		return this.spriteSide;
	}

	@OnlyIn(Dist.CLIENT)
	public void renderItem(
			final PipeBlock<?, ?> block, final ItemStack stack, final ItemDisplayContext transformType, final boolean leftHand, final PoseStack matrixStack, final MultiBufferSource buffer,
			final int combinedLight, final int combinedOverlay, final BakedModel model
	) {
		IItemRendererProvider.disabled.set(true);
		Minecraft.getInstance().getItemRenderer().render(stack, transformType, leftHand, matrixStack, buffer, combinedLight, combinedOverlay,
				(BakedModelItemDefaults) (state, direction, random) -> this.itemModelCache.computeIfAbsent(
						Optional.ofNullable(direction),
						direction1 -> this.bakeQuads(direction1.orElse(null), PipeNetHelper.ITEM_CONNECTIONS)
				)
		);
		IItemRendererProvider.disabled.set(false);
	}

	@OnlyIn(Dist.CLIENT)
	public void registerTextureAtlas(final Consumer<ResourceLocation> register) {
		this.itemModelCache.clear();
		register.accept(this.textureSide.get().getValue());
		register.accept(this.textureEnd.get().getValue());
		if (this.sideOverlayTexture != null) {
			register.accept(this.sideOverlayTexture.getValue());
		}
		if (this.endOverlayTexture != null) {
			register.accept(this.endOverlayTexture.getValue());
		}
		this.spriteSide = null;
		this.spriteEnd = null;
		this.spriteEndOverlay = null;
	}
}
