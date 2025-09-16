package conductance.init.block;

import net.minecraft.world.level.block.Block;
import lombok.Getter;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;

public final class MaterialBlock extends Block {

	private final @Getter Material material;
	private final @Getter MaterialGenerationHandler handler;

	public MaterialBlock(final Properties properties, final Material material, final MaterialGenerationHandler handler) {
		super(properties);
		this.material = material;
		this.handler = handler;
	}
}
