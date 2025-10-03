package conductance.client.model;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.SimpleModelWrapper;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.core.BlockMath;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import com.mojang.math.Quadrant;
import com.mojang.math.Transformation;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionf;

/**
 * Parts of this code have been inspired by and/or adapted from Applied Energistics 2 (Licensed LGPL 3.0)
 *
 * @see net.minecraft.client.renderer.block.model.Variant
 * @see <a href="https://github.com/AppliedEnergistics/Applied-Energistics-2">Applied Energistics 2</a>
 */
record ExtendedRotationVariant(ResourceLocation modelLocation, ExtendedRotationVariant.SimpleModelState modelState) implements BlockModelPart.Unbaked {
	public static final MapCodec<ExtendedRotationVariant> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		ResourceLocation.CODEC.fieldOf("model").forGetter(ExtendedRotationVariant::modelLocation),
		ExtendedRotationVariant.SimpleModelState.MAP_CODEC.forGetter(ExtendedRotationVariant::modelState)
	).apply(instance, ExtendedRotationVariant::new));

	@Override
	public BlockModelPart bake(final ModelBaker baker) {
		return SimpleModelWrapper.bake(baker, this.modelLocation, this.modelState.asModelState());
	}

	@Override
	public void resolveDependencies(final ResolvableModel.Resolver resolver) {
		resolver.markDependency(this.modelLocation);
	}

	public record SimpleModelState(Quadrant x, Quadrant y, Quadrant z, boolean uvLock) {
		public static final MapCodec<SimpleModelState> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Quadrant.CODEC.optionalFieldOf("x", Quadrant.R0).forGetter(SimpleModelState::x),
			Quadrant.CODEC.optionalFieldOf("y", Quadrant.R0).forGetter(SimpleModelState::y),
			Quadrant.CODEC.optionalFieldOf("z", Quadrant.R0).forGetter(SimpleModelState::z),
			Codec.BOOL.optionalFieldOf("uvlock", false).forGetter(SimpleModelState::uvLock)
		).apply(instance, SimpleModelState::new));
		private static final ModelState[] MODEL_STATES = SimpleModelState.createTransformations(false);
		private static final ModelState[] MODEL_STATES_UV = SimpleModelState.createTransformations(true);

		private static ModelState[] createTransformations(final boolean uvLocked) {
			final ModelState[] result = new ModelState[64];
			final Quadrant[] quadrants = Quadrant.values();
			final float[] angles = new float[] {0, 90, 180, 270};
			for (final Quadrant xRotation : quadrants) {
				for (final Quadrant yRotation : quadrants) {
					result[SimpleModelState.indexFromAngles(xRotation, yRotation, Quadrant.R0)] = Util.make(() -> {
						final BlockModelRotation blockModelRotation = BlockModelRotation.by(xRotation, yRotation);
						return uvLocked ? blockModelRotation.withUvLock() : blockModelRotation;
					});
					for (final Quadrant zRotation : quadrants) {
						if (zRotation == Quadrant.R0) {
							continue;
						}
						final int index = SimpleModelState.indexFromAngles(xRotation, yRotation, zRotation);
						final Transformation transformation = new Transformation(new Matrix4f().identity().rotate(new Quaternionf().rotateYXZ(
							-angles[yRotation.shift] * Mth.DEG_TO_RAD,
							-angles[xRotation.shift] * Mth.DEG_TO_RAD,
							-angles[zRotation.shift] * Mth.DEG_TO_RAD
						)));
						if (uvLocked) {
							final EnumMap<Direction, Matrix4fc> faceMapping = new EnumMap<>(Direction.class);
							final EnumMap<Direction, Matrix4fc> inverseFaceMapping = new EnumMap<>(Direction.class);
							for (final Direction direction : Direction.values()) {
								final Matrix4fc matrix4fc = BlockMath.getFaceTransformation(transformation, direction).getMatrix();
								faceMapping.put(direction, matrix4fc);
								inverseFaceMapping.put(direction, matrix4fc.invertAffine(new Matrix4f()));
							}
							result[index] = new SpinnableModelState(transformation, faceMapping, inverseFaceMapping);
						} else {
							result[index] = new SpinnableModelState(transformation, Collections.emptyMap(), Collections.emptyMap());
						}
					}
				}
			}
			return result;
		}

		private static int indexFromAngles(final Quadrant xRotation, final Quadrant yRotation, final Quadrant zRotation) {
			return xRotation.shift * 16 + yRotation.shift * 4 + zRotation.shift;
		}

		public ModelState asModelState() {
			final int index = SimpleModelState.indexFromAngles(this.x, this.y, this.z);
			return this.uvLock ? SimpleModelState.MODEL_STATES_UV[index] : SimpleModelState.MODEL_STATES[index];
		}

		private record SpinnableModelState(Transformation transformation, Map<Direction, Matrix4fc> faceTransformations, Map<Direction, Matrix4fc> inverseFaceTransformations) implements ModelState {
			@Override
			public Matrix4fc faceTransformation(final Direction face) {
				return this.faceTransformations.getOrDefault(face, ModelState.NO_TRANSFORM);
			}

			@Override
			public Matrix4fc inverseFaceTransformation(final Direction face) {
				return this.inverseFaceTransformations.getOrDefault(face, ModelState.NO_TRANSFORM);
			}
		}
	}
}
