package conductance.core.material;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.function.TriConsumer;
import org.jetbrains.annotations.Nullable;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.material.event.RegisterMaterialOverrideEvent;

@RequiredArgsConstructor
final class RegisterMaterialOverrideEventImpl implements RegisterMaterialOverrideEvent {

	private final TriConsumer<MaterialGenerationHandler, Material, Block> blockDelegate;
	private final TriConsumer<MaterialGenerationHandler, Material, Item> itemDelegate;
	private final TriConsumer<MaterialGenerationHandler, Material, Fluid> fluidDelegate;

	@Override
	public void add(final MaterialGenerationHandler handler, final Material material, @Nullable final Block block) {
		this.blockDelegate.accept(handler, material, block);
		this.itemDelegate.accept(handler, material, block != null ? block.asItem() : null);
	}

	@Override
	public void add(final MaterialGenerationHandler handler, final Material material, @Nullable final Item item) {
		this.itemDelegate.accept(handler, material, item);
	}

	@Override
	public void add(final MaterialGenerationHandler handler, final Material material, @Nullable final Fluid fluid) {
		this.fluidDelegate.accept(handler, material, fluid);
	}
}
