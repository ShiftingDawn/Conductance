package conductance.lib.pack.client;

import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import com.mojang.math.Quadrant;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import conductance.api.resource.ModelElementFaceBuilder;
import conductance.api.resource.ModelNeoforgeDataBuilder;

@RequiredArgsConstructor
final class ModelElementFaceBuilderImpl extends JsonResourceBuilderImpl<ModelElementFaceBuilder> implements ModelElementFaceBuilder {

	private final int[] fromPos;
	private final int[] toPos;
	private final Direction face;
	private int[] uv = new int[0];
	private @Nullable String texture;
	private @Nullable Direction cullFace;
	private @Nullable Quadrant rotation;
	private int tintIndex = -1;
	private @Nullable ModelNeoforgeDataBuilderImpl neoforgeData;

	@Override
	public ModelElementFaceBuilder uv(final int x1, final int y1, final int x2, final int y2) {
		this.uv = new int[] {x1, y1, x2, y2};
		return this;
	}

	@Override
	public ModelElementFaceBuilder uvAuto() {
		return switch (this.face) {
			case UP -> this.uv(this.fromPos[0], this.fromPos[2], this.toPos[0], this.toPos[2]);
			case DOWN -> this.uv(this.fromPos[0], this.toPos[2], this.toPos[0], this.fromPos[2]);
			case NORTH -> this.uv(this.toPos[0], this.toPos[1], this.fromPos[0], this.fromPos[1]);
			case SOUTH -> this.uv(this.fromPos[0], this.toPos[1], this.toPos[0], this.fromPos[1]);
			case WEST -> this.uv(this.fromPos[2], this.toPos[1], this.toPos[2], this.fromPos[1]);
			case EAST -> this.uv(this.toPos[2], this.toPos[1], this.fromPos[2], this.fromPos[1]);
		};
	}

	@Override
	public ModelElementFaceBuilder texture(final String textureKey) {
		this.texture = '#' + textureKey;
		return this;
	}

	@Override
	public ModelElementFaceBuilder cullFace(final Direction face) {
		this.cullFace = face;
		return this;
	}

	@Override
	public ModelElementFaceBuilder rotation(final Quadrant newRotation) {
		this.rotation = newRotation;
		return this;
	}

	@Override
	public ModelElementFaceBuilder tintIndex(final int newTintIndex) {
		this.tintIndex = newTintIndex;
		return this;
	}

	@Override
	public ModelElementFaceBuilder neoforgeData(final Consumer<ModelNeoforgeDataBuilder> builder) {
		this.neoforgeData = Util.make(new ModelNeoforgeDataBuilderImpl(), builder);
		return this;
	}

	@Override
	protected void populateJson(final JsonObject json) {
		if (this.uv.length > 0) {
			json.add("uv", Util.make(new JsonArray(), arr -> {
				arr.add(this.uv[0]);
				arr.add(this.uv[1]);
				arr.add(this.uv[2]);
				arr.add(this.uv[3]);
			}));
		}
		if (this.texture != null) {
			json.addProperty("texture", this.texture);
		}
		if (this.cullFace != null) {
			json.addProperty("cullface", this.cullFace.getSerializedName());
		}
		if (this.rotation != null) {
			json.addProperty("rotation", switch (this.rotation) {
				case R0 -> 0;
				case R90 -> 90;
				case R180 -> 180;
				case R270 -> 270;
			});
		}
		if (this.tintIndex != -1) {
			json.addProperty("tintindex", this.tintIndex);
		}
		if (this.neoforgeData != null) {
			json.add("neoforge_data", this.neoforgeData.build());
		}
	}
}
