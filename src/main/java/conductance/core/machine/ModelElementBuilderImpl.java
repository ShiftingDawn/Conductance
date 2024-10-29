package conductance.core.machine;

import java.util.EnumMap;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;
import conductance.api.resource.ModelElementBuilder;
import conductance.api.resource.ModelElementFaceBuilder;
import conductance.api.util.SerializationHelper;

final class ModelElementBuilderImpl<BUILDER extends ModelBuilderImpl<BUILDER>> implements ModelElementBuilder<BUILDER> {

	private final int[] from = new int[] {0, 0, 0};
	private final int[] to = new int[] {16, 16, 16};
	private final EnumMap<Direction, ModelElementFaceBuilderImpl<BUILDER>> faces = new EnumMap<>(Direction.class);
	private final BUILDER builder;
	@Nullable
	private Rotation rotation;
	@Nullable
	private Boolean shade;
	@Nullable
	private Integer lightEmission = null;


	ModelElementBuilderImpl(final BUILDER builder) {
		this.builder = builder;
	}

	@Override
	public ModelElementBuilder<BUILDER> from(final int x, final int y, final int z) {
		this.from[0] = x;
		this.from[1] = y;
		this.from[2] = z;
		return this;
	}

	@Override
	public ModelElementBuilder<BUILDER> to(final int x, final int y, final int z) {
		this.to[0] = x;
		this.to[1] = y;
		this.to[2] = z;
		return this;
	}

	@Override
	public ModelElementBuilder<BUILDER> rotation(final int originX, final int originY, final int originZ, final Direction.Axis axis, final float angle, final boolean rescale) {
		this.rotation = new Rotation(originX, originY, originZ, axis, angle, rescale);
		return this;
	}

	@Override
	public ModelElementBuilder<BUILDER> shade(final boolean newShade) {
		this.shade = newShade;
		return this;
	}

	@Override
	public ModelElementBuilder<BUILDER> lightEmission(final int newLightEmission) {
		this.lightEmission = newLightEmission;
		return this;
	}

	@Override
	public ModelElementFaceBuilder<BUILDER> face(final Direction face) {
		return this.faces.computeIfAbsent(face, k -> new ModelElementFaceBuilderImpl<>(this));
	}

	@Override
	public BUILDER build() {
		return this.builder;
	}

	JsonElement serialize() {
		return Util.make(new JsonObject(), json -> {
			json.add("from", SerializationHelper.toJsonArray(this.from));
			json.add("to", SerializationHelper.toJsonArray(this.to));
			if (this.rotation != null) {
				json.add("rotation", Util.make(new JsonObject(), rot -> {
					rot.add("origin", SerializationHelper.toJsonArray(this.rotation.originX, this.rotation.originY, this.rotation.originZ));
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
