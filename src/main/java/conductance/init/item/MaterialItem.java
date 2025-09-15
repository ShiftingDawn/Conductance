package conductance.init.item;

import net.minecraft.world.item.Item;
import lombok.Getter;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;

public final class MaterialItem extends Item {

	private final @Getter Material material;
	private final @Getter MaterialGenerationHandler handler;

	public MaterialItem(final Properties properties, final Material material, final MaterialGenerationHandler handler) {
		super(properties);
		this.material = material;
		this.handler = handler;
	}
}
