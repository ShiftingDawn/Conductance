package conductance.api.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.SimpleUnbakedGeometry;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TriState;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.math.Quadrant;
import net.neoforged.neoforge.client.model.IQuadTransformer;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import conductance.api.CAPI;

public final class ModelUtils {

	public static final ResourceLocation BLOCK_ATLAS = ResourceLocation.withDefaultNamespace("textures/atlas/blocks.png");
	public static final ResourceLocation MODEL_CUBE_PARTICLE = ResourceLocation.fromNamespaceAndPath(CAPI.MOD_ID, "block/cube_all");
	public static final Map<Direction, IntPos> MODEL_ROTATION;
	public static final Map<Direction, String> LOGICAL_SIDES;
	public static final Vector3fc VECTOR_BLOCK_START = new Vector3f(0, 0, 0);
	public static final Vector3fc VECTOR_BLOCK_END = new Vector3f(16, 16, 16);
	public static final BlockElement BLOCK_ELEMENT_CUBE = ModelUtils.createCubeBlockElement(false);
	public static final BlockElement BLOCK_ELEMENT_CUBE_EMISSIVE = ModelUtils.createCubeBlockElement(true);

	public static @UnknownNullability TextureAtlasSprite getBlockSprite(@Nullable final ResourceLocation texture) {
		return Minecraft.getInstance().getTextureAtlas(ModelUtils.BLOCK_ATLAS).apply(Objects.requireNonNullElseGet(texture, MissingTextureAtlasSprite::getLocation));
	}

	public static BakedQuad createFullCubeBakedQuad(final boolean emissive, final Direction face, final ResourceLocation texture) {
		final BlockElement element = !emissive ? ModelUtils.BLOCK_ELEMENT_CUBE : ModelUtils.BLOCK_ELEMENT_CUBE_EMISSIVE;
		return SimpleUnbakedGeometry.bakeFace(element, element.faces().get(face), ModelUtils.getBlockSprite(texture), face, BlockModelRotation.X0_Y0);
	}

	public static BakedQuad retextureBakedQuad(final BakedQuad quad, final TextureAtlasSprite sprite, final boolean emissive) {
		final TextureAtlasSprite oldSprite = quad.sprite();
		final int[] vertices = quad.vertices().clone();
		for (int i = 0; i < 4; i++) {
			final int offset = i * IQuadTransformer.STRIDE + IQuadTransformer.UV0;
			float u = Float.intBitsToFloat(vertices[offset]);
			float v = Float.intBitsToFloat(vertices[offset + 1]);
			u = Mth.map(u, oldSprite.getU0(), oldSprite.getU1(), sprite.getU0(), sprite.getU1());
			v = Mth.map(v, oldSprite.getV0(), oldSprite.getV1(), sprite.getV0(), sprite.getV1());
			vertices[offset] = Float.floatToRawIntBits(u);
			vertices[offset + 1] = Float.floatToRawIntBits(v);
		}
		return new BakedQuad(vertices, quad.tintIndex(), quad.direction(), sprite, !emissive, emissive ? 15 : quad.lightEmission(), quad.hasAmbientOcclusion());
	}

	public static BakedQuad[] getBaseQuadsFromBlockState(final BlockState state, final BlockAndTintGetter level, final BlockPos pos, final RandomSource random) {
		final List<BlockModelPart> newParts = Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(state).collectParts(level, pos, state, random);
		if (newParts.isEmpty()) {
			return new BakedQuad[0];
		}
		final BakedQuad[] result = new BakedQuad[6];
		for (final Direction face : Direction.values()) {
			final List<BakedQuad> quads = newParts.getFirst().getQuads(face);
			result[face.get3DDataValue()] = !quads.isEmpty() ? quads.getFirst() : null;
		}
		return result;
	}

	private static BlockElement createCubeBlockElement(final boolean emissive) {
		final Map<Direction, BlockElementFace> faces = new EnumMap<>(Direction.class);
		for (final Direction face : Direction.values()) {
			faces.put(face, new BlockElementFace(face, BlockElementFace.NO_TINT, "particle", null, Quadrant.R0));
		}
		return new BlockElement(ModelUtils.VECTOR_BLOCK_START, ModelUtils.VECTOR_BLOCK_END, faces, null, !emissive, emissive ? 15 : 0);
	}

	static {
		MODEL_ROTATION = Collections.unmodifiableMap(Util.make(new EnumMap<>(Direction.class), map -> {
			map.put(Direction.UP, new IntPos(270, 0));
			map.put(Direction.DOWN, new IntPos(90, 0));
			map.put(Direction.NORTH, new IntPos(0, 0));
			map.put(Direction.EAST, new IntPos(0, 90));
			map.put(Direction.SOUTH, new IntPos(0, 180));
			map.put(Direction.WEST, new IntPos(0, 270));
		}));
		LOGICAL_SIDES = Collections.unmodifiableMap(Util.make(new EnumMap<>(Direction.class), map -> {
			map.put(Direction.UP, "top");
			map.put(Direction.DOWN, "bottom");
			map.put(Direction.NORTH, "front");
			map.put(Direction.EAST, "side");
			map.put(Direction.SOUTH, "back");
			map.put(Direction.WEST, "side");
		}));
	}

	public record ReplacedQuadBlockModelPart(BlockModelPart original, BakedQuad[] quads) implements BlockModelPart {

		@Override
		public List<BakedQuad> getQuads(@Nullable final Direction direction) {
			final ArrayList<BakedQuad> result = new ArrayList<>(this.original.getQuads(direction));
			if (direction != null) {
				final BakedQuad quad = this.quads[direction.get3DDataValue()];
				if (quad != null) {
					if (result.isEmpty()) {
						result.add(quad);
					} else {
						result.set(0, quad);
					}
				}
			}
			return result;
		}

		@Override
		public ChunkSectionLayer getRenderType(final BlockState state) {
			return this.original.getRenderType(state);
		}

		@Override
		public TriState ambientOcclusion() {
			return this.original.ambientOcclusion();
		}

		@SuppressWarnings("deprecation")
		@Override
		public boolean useAmbientOcclusion() {
			return this.original.useAmbientOcclusion();
		}

		@Override
		public TextureAtlasSprite particleIcon() {
			return this.original.particleIcon();
		}
	}

	public record SimpleOverlayQuadBlockModelPart(Map<Direction, List<BakedQuad>> quads, TextureAtlasSprite particleIcon) implements BlockModelPart {

		@Override
		public List<BakedQuad> getQuads(@Nullable final Direction direction) {
			final List<BakedQuad> result = direction != null ? this.quads.get(direction) : null;
			return result != null ? result : Collections.emptyList();
		}

		@Override
		public boolean useAmbientOcclusion() {
			return true;
		}

		@Override
		public ChunkSectionLayer getRenderType(final BlockState state) {
			return ChunkSectionLayer.CUTOUT_MIPPED;
		}
	}

	private ModelUtils() {
	}
}
