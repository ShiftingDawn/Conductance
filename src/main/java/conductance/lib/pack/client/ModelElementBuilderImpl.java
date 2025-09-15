package conductance.lib.pack.client;

import java.util.EnumMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;
import conductance.api.resource.ModelElementBuilder;
import conductance.api.resource.ModelElementFaceBuilder;
import conductance.api.resource.ModelNeoforgeDataBuilder;

final class ModelElementBuilderImpl extends JsonResourceBuilderImpl<ModelElementBuilder> implements ModelElementBuilder {

	private final int[] from = new int[] {0, 0, 0};
	private final int[] to = new int[] {16, 16, 16};
	private final EnumMap<Direction, ModelElementFaceBuilderImpl> faces = new EnumMap<>(Direction.class);
	private @Nullable Rotation rotation;
	private @Nullable Boolean shade;
	private @Nullable Integer lightEmission = null;
	private @Nullable ModelNeoforgeDataBuilderImpl neoforgeData;

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

	@Override
	public ModelElementBuilder neoforgeData(final Consumer<ModelNeoforgeDataBuilder> builder) {
		this.neoforgeData = Util.make(new ModelNeoforgeDataBuilderImpl(), builder);
		return this;
	}

	boolean isEmpty() {
		return this.faces.isEmpty();
	}

	@Override
	protected void populateJson(final JsonObject json) {
		json.add("from", Util.make(new JsonArray(3), arr -> {
			arr.add(this.from[0]);
			arr.add(this.from[1]);
			arr.add(this.from[2]);
		}));
		json.add("to", Util.make(new JsonArray(3), arr -> {
			arr.add(this.to[0]);
			arr.add(this.to[1]);
			arr.add(this.to[2]);
		}));
		if (this.rotation != null) {
			json.add("rotation", Util.make(new JsonObject(), rot -> {
				rot.add("origin", Util.make(new JsonArray(), arr -> {
					arr.add(this.rotation.originX);
					arr.add(this.rotation.originY);
					arr.add(this.rotation.originZ);
				}));
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
			this.faces.forEach((face, faceBuilder) -> facesDef.add(face.getSerializedName(), faceBuilder.build()));
		}));
		if (this.neoforgeData != null) {
			json.add("neoforge_data", this.neoforgeData.build());
		}
	}

	private record Rotation(int originX, int originY, int originZ, Direction.Axis axis, float angle, boolean rescale) {

	}
}
