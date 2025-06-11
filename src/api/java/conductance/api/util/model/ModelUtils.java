package conductance.api.util.model;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import conductance.api.util.world.RotationState;

public final class ModelUtils {

	public static final Map<Direction, Tuple<Integer, Integer>> MODEL_ROTATION;
	public static final Map<Direction, String> LOGICAL_SIDES;
	public static final FaceBakery FACE_BAKERY = new FaceBakery();
	private static final Vector3f BLOCK_START = new Vector3f(0, 0, 0);
	private static final Vector3f BLOCK_END = new Vector3f(16, 16, 16);

	public static TextureAtlasSprite getSprite(final ResourceLocation texture) {
		return Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS).getSprite(texture);
	}

	public static BakedQuad bakeFace(final Vector3f posFrom, final Vector3f posTo, final Direction facing, final TextureAtlasSprite sprite, final ModelState rotation, final int tintIndex, final boolean cull,
			final boolean shade) {
		final BlockElementFace blockElementFace = new BlockElementFace(cull ? facing : null, tintIndex, "", new BlockFaceUV(new float[] {0f, 0f, 16f, 16f}, 0));
		return ModelUtils.FACE_BAKERY.bakeQuad(posFrom, posTo, blockElementFace, sprite, facing, rotation, null, shade);
	}

	public static BakedQuad bakeFace(final Direction facing, final TextureAtlasSprite sprite, final ModelState rotation, final int tintIndex, final boolean cull, final boolean shade) {
		return ModelUtils.bakeFace(ModelUtils.BLOCK_START, ModelUtils.BLOCK_END, facing, sprite, rotation, tintIndex, false, false);
	}

	public static Direction getRotationFromState(@Nullable final BlockState state) {
		if (state != null) {
			for (final RotationState rotationState : RotationState.values()) {
				if (state.hasProperty(rotationState.property)) {
					return state.getValue(rotationState.property);
				}
			}
		}
		return Direction.NORTH;
	}

	public static ModelState getModelRotationState(final Direction facing) {
		return switch (facing) {
			case DOWN -> BlockModelRotation.X90_Y0;
			case UP -> BlockModelRotation.X270_Y0;
			case NORTH -> BlockModelRotation.X0_Y0;
			case SOUTH -> BlockModelRotation.X0_Y180;
			case WEST -> BlockModelRotation.X0_Y270;
			case EAST -> BlockModelRotation.X0_Y90;
		};
	}

	static {
		MODEL_ROTATION = Collections.unmodifiableMap(Util.make(new EnumMap<>(Direction.class), map -> {
			map.put(Direction.UP, new Tuple<>(270, 0));
			map.put(Direction.DOWN, new Tuple<>(90, 0));
			map.put(Direction.NORTH, new Tuple<>(0, 0));
			map.put(Direction.EAST, new Tuple<>(0, 90));
			map.put(Direction.SOUTH, new Tuple<>(0, 180));
			map.put(Direction.WEST, new Tuple<>(0, 270));
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

	private ModelUtils() {
	}
}
