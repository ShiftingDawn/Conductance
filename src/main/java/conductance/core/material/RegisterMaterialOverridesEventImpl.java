package conductance.core.material;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.function.TriConsumer;
import org.jetbrains.annotations.Nullable;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.material.event.RegisterMaterialOverridesEvent;

@RequiredArgsConstructor
final class RegisterMaterialOverridesEventImpl implements RegisterMaterialOverridesEvent {

	private final TriConsumer<Material, MaterialGenerationHandler, Block> blockDelegate;
	private final TriConsumer<Material, MaterialGenerationHandler, Item> itemDelegate;

	@Override
	public void add(final Material material, final MaterialGenerationHandler handler, @Nullable final Block block) {
		this.blockDelegate.accept(material, handler, block);
		this.itemDelegate.accept(material, handler, block != null ? block.asItem() : null);
	}

	@Override
	public void add(final Material material, final MaterialGenerationHandler handler, @Nullable final Item item) {
		this.itemDelegate.accept(material, handler, item);
	}
}
