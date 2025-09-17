package conductance.init.block;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.Block;
import lombok.Getter;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;

public final class MaterialBlock extends Block {

	private final @Getter Material material;
	private final @Getter MaterialGenerationHandler handler;
	private final MutableComponent name;

	public MaterialBlock(final Properties properties, final Material material, final MaterialGenerationHandler handler) {
		super(properties);
		this.material = material;
		this.handler = handler;
		this.name = Component.translatable(handler.makeDescriptionId(material), Component.translatable(material.getDescriptionId()));
	}

	@Override
	public MutableComponent getName() {
		return this.name;
	}
}
