package conductance.core.material;

import java.util.Objects;
import java.util.Optional;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.material.MaterialRegistry;

@SuppressWarnings("DataFlowIssue")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class MaterialRegistryImpl implements MaterialRegistry {

	private final Table<Material, MaterialGenerationHandler, Block> blocks = HashBasedTable.create();
	private final Table<Material, MaterialGenerationHandler, Item> items = HashBasedTable.create();
	private final Table<Material, MaterialGenerationHandler, Fluid> fluids = HashBasedTable.create();
	private final Table<Material, MaterialGenerationHandler, Optional<Block>> overriddenBlocks = HashBasedTable.create();
	private final Table<Material, MaterialGenerationHandler, Optional<Item>> overriddenItems = HashBasedTable.create();
	private final Table<Material, MaterialGenerationHandler, Optional<Fluid>> overriddenFluids = HashBasedTable.create();

	public void register(final Material material, final MaterialGenerationHandler handler, final Block block) {
		this.blocks.put(material, handler, block);
	}

	public void register(final Material material, final MaterialGenerationHandler handler, final Item item) {
		this.items.put(material, handler, item);
	}

	public void register(final Material material, final MaterialGenerationHandler handler, final Fluid fluid) {
		this.fluids.put(material, handler, fluid);
	}

	@Override
	@UnknownNullability
	public Block getBlock(final Material material, final MaterialGenerationHandler handler) {
		if (this.hasBlockOverride(material, handler)) {
			return this.overriddenBlocks.get(material, handler).orElse(null);
		} else {
			return this.blocks.get(material, handler);
		}
	}

	@Override
	@UnknownNullability
	public Item getItem(final Material material, final MaterialGenerationHandler handler) {
		if (this.hasItemOverride(material, handler)) {
			return this.overriddenItems.get(material, handler).orElse(null);
		} else {
			return this.items.get(material, handler);
		}
	}

	@Override
	public Fluid getFluid(final Material material, final MaterialGenerationHandler handler) {
		if (this.hasFluidOverride(material, handler)) {
			return this.overriddenFluids.get(material, handler).orElse(null);
		} else {
			return this.fluids.get(material, handler);
		}
	}

	@Override
	public boolean hasBlockOverride(final Material material, final MaterialGenerationHandler handler) {
		return this.overriddenBlocks.contains(material, handler);
	}

	@Override
	public boolean hasItemOverride(final Material material, final MaterialGenerationHandler handler) {
		return this.overriddenItems.contains(material, handler);
	}

	@Override
	public boolean hasFluidOverride(final Material material, final MaterialGenerationHandler handler) {
		return this.overriddenFluids.contains(material, handler);
	}

	public void addOverride(final MaterialGenerationHandler handler, final Material material, @Nullable final Block block) {
		Objects.requireNonNull(handler, "handler cannot be null");
		Objects.requireNonNull(material, "material cannot be null");
		this.overriddenBlocks.put(material, handler, Optional.ofNullable(block));
	}

	public void addOverride(final MaterialGenerationHandler handler, final Material material, @Nullable final Item item) {
		Objects.requireNonNull(handler, "handler cannot be null");
		Objects.requireNonNull(material, "material cannot be null");
		this.overriddenItems.put(material, handler, Optional.ofNullable(item));
	}

	public void addOverride(final MaterialGenerationHandler handler, final Material material, @Nullable final Fluid fluid) {
		Objects.requireNonNull(handler, "handler cannot be null");
		Objects.requireNonNull(material, "material cannot be null");
		this.overriddenFluids.put(material, handler, Optional.ofNullable(fluid));
	}

	public Table<Material, MaterialGenerationHandler, Block> getBlockTable() {
		return this.blocks;
	}

	public Table<Material, MaterialGenerationHandler, Item> getItemTable() {
		return this.items;
	}

	public Table<Material, MaterialGenerationHandler, Fluid> getFluidTable() {
		return this.fluids;
	}
}
