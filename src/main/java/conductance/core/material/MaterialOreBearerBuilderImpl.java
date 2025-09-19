package conductance.core.material;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import conductance.api.material.MaterialOreBearer;
import conductance.api.material.event.MaterialOreBearerBuilder;

@RequiredArgsConstructor
final class MaterialOreBearerBuilderImpl implements MaterialOreBearerBuilder {

	private final @Getter ResourceLocation bearingBlockModel;
	private final @Getter MapColor mapColor;
	private final @Getter SoundType soundType;
	private MaterialOreBearer.BlockType blockType = MaterialOreBearer.BlockType.DEFAULT;
	private boolean doubleOutput = false;
	private boolean gravity = false;

	@Override
	public MaterialOreBearerBuilder blockType(final MaterialOreBearer.BlockType type) {
		this.blockType = type;
		return this;
	}

	@Override
	public MaterialOreBearerBuilder doubleOutput() {
		this.doubleOutput = true;
		return this;
	}

	@Override
	public MaterialOreBearerBuilder hasGravity() {
		this.gravity = true;
		return this;
	}

	public MaterialOreBearerImpl build() {
		return new MaterialOreBearerImpl(this.bearingBlockModel, this.mapColor, this.soundType, this.blockType, this.doubleOutput, this.gravity);
	}
}
