package conductance.core.runtimepack.client;

import java.util.EnumMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;
import conductance.api.resource.ModelElementBuilder;
import conductance.api.resource.ModelElementFaceBuilder;
import conductance.api.util.JsonUtils;

final class ModelElementBuilderImpl implements ModelElementBuilder {

	private final int[] from = new int[] {0, 0, 0};
	private final int[] to = new int[] {16, 16, 16};
	private final EnumMap<Direction, ModelElementFaceBuilderImpl> faces = new EnumMap<>(Direction.class);
	@Nullable
	private Rotation rotation;
	@Nullable
	private Boolean shade;
	@Nullable
	private Integer lightEmission = null;

	@Override
	public ModelElementBuilder from(final int x, final int y, final int z) {
		this.from[0] = x;
		this.from[1] = y;
		this.from[2] = z;
		return this;
	}

	@Override
	public ModelElementBuilder to(final int x, final int y, final int z) {
		this.to[0] = x;
		this.to[1] = y;
		this.to[2] = z;
		return this;
	}

	@Override
	public ModelElementBuilder rotation(final int originX, final int originY, final int originZ, final Direction.Axis axis, final float angle, final boolean rescale) {
		this.rotation = new Rotation(originX, originY, originZ, axis, angle, rescale);
		return this;
	}

	@Override
	public ModelElementBuilder shade(final boolean newShade) {
		this.shade = newShade;
		return this;
	}

	@Override
	public ModelElementBuilder lightEmission(final int newLightEmission) {
		this.lightEmission = newLightEmission;
		return this;
	}

	@Override
	public ModelElementBuilder face(final Direction face, final Consumer<ModelElementFaceBuilder> builder) {
		Util.make(this.faces.computeIfAbsent(face, k -> new ModelElementFaceBuilderImpl()), builder);
		return this;
	}

	@Override
	public ModelElementBuilder faces(final BiConsumer<Direction, ModelElementFaceBuilder> faceBuilder, final boolean cull, final Direction... facesToMake) {
		for (final Direction face : facesToMake.length > 0 ? facesToMake : Direction.values()) {
			this.face(face, elementFaceBuilder -> {
				if (cull) {
					elementFaceBuilder.cullFace(face);
				}
				faceBuilder.accept(face, elementFaceBuilder);
			});
		}
		return this;
	}

	boolean isEmpty() {
		return this.faces.isEmpty();
	}

	JsonElement serialize() {
		return Util.make(new JsonObject(), json -> {
			json.add("from", JsonUtils.toJsonArray(this.from));
			json.add("to", JsonUtils.toJsonArray(this.to));
			if (this.rotation != null) {
				json.add("rotation", Util.make(new JsonObject(), rot -> {
					rot.add("origin", JsonUtils.toJsonArray(this.rotation.originX, this.rotation.originY, this.rotation.originZ));
					rot.addProperty("axis", this.rotation.axis.getSerializedName());
					rot.addProperty("angle", this.rotation.angle);
					rot.addProperty("rescale", this.rotation.rescale);
				}));
			}
			if (this.shade != null) {
				json.addProperty("shade", this.shade);
			}
			if (this.lightEmission != null) {
				json.addProperty("light_emission", this.lightEmission);
			}
			json.add("faces", Util.make(new JsonObject(), facesDef -> {
				this.faces.forEach((face, faceBuilder) -> facesDef.add(face.getSerializedName(), faceBuilder.serialize()));
			}));
		});
	}

	private record Rotation(int originX, int originY, int originZ, Direction.Axis axis, float angle, boolean rescale) {

	}
}
