package conductance.lib.pack.client;

import net.minecraft.resources.ResourceLocation;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;
import conductance.api.resource.BlockStateModelPropsBuilder;

final class BlockStateModelPropsBuilderImpl extends JsonResourceBuilderImpl<BlockStateModelPropsBuilder> implements BlockStateModelPropsBuilder {

	private final ResourceLocation model;
	private @Nullable Integer x;
	private @Nullable Integer y;
	private @Nullable Boolean uvLock;
	private @Nullable Integer weight;

	BlockStateModelPropsBuilderImpl(final ResourceLocation model) {
		this.model = model;
	}

	@Override
	public BlockStateModelPropsBuilder x(final int newX) {
		this.x = newX;
		return this;
	}

	@Override
	public BlockStateModelPropsBuilder y(final int newY) {
		this.y = newY;
		return this;
	}

	@Override
	public BlockStateModelPropsBuilder uvLock(final boolean newUvLock) {
		this.uvLock = newUvLock;
		return this;
	}

	@Override
	public BlockStateModelPropsBuilder weight(final int newWeight) {
		this.weight = newWeight;
		return this;
	}

	@Override
	protected void populateJson(final JsonObject json) {
		json.addProperty("model", this.model.toString());
		if (this.x != null) {
			json.addProperty("x", this.x);
		}
		if (this.y != null) {
			json.addProperty("y", this.y);
		}
		if (this.uvLock != null) {
			json.addProperty("uvlock", this.uvLock);
		}
		if (this.weight != null) {
			json.addProperty("weight", this.weight);
		}
	}
}
