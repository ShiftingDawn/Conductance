package conductance.core.material;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import conductance.api.material.MaterialOreBearer;

@RequiredArgsConstructor
final class MaterialOreBearerImpl implements MaterialOreBearer {

	private final @Getter ResourceLocation bearingBlockModel;
	private final @Getter MapColor mapColor;
	private final @Getter SoundType soundType;
	private final @Getter BlockType blockType;
	private final boolean doubleOutput;
	private final boolean gravity;

	@Override
	public boolean hasDoubleOutput() {
		return this.doubleOutput;
	}

	@Override
	public boolean hasGravity() {
		return this.gravity;
	}
}
