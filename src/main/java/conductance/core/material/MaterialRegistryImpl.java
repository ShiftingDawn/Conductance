package conductance.core.material;

import java.util.Objects;
import java.util.Optional;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
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
	private final Table<Material, MaterialGenerationHandler, Optional<Block>> overriddenBlocks = HashBasedTable.create();
	private final Table<Material, MaterialGenerationHandler, Optional<Item>> overriddenItems = HashBasedTable.create();

	public void register(final Material material, final MaterialGenerationHandler handler, final Block block) {
		this.blocks.put(material, handler, block);
	}

	public void register(final Material material, final MaterialGenerationHandler handler, final Item item) {
		this.items.put(material, handler, item);
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

	public boolean hasBlockOverride(final Material material, final MaterialGenerationHandler handler) {
		return this.overriddenBlocks.contains(material, handler);
	}

	public boolean hasItemOverride(final Material material, final MaterialGenerationHandler handler) {
		return this.overriddenItems.contains(material, handler);
	}

	public void addOverride(final Material material, final MaterialGenerationHandler handler, @Nullable final Block block) {
		Objects.requireNonNull(material, "material cannot be null");
		Objects.requireNonNull(handler, "handler cannot be null");
		this.overriddenBlocks.put(material, handler, Optional.ofNullable(block));
	}

	public void addOverride(final Material material, final MaterialGenerationHandler handler, @Nullable final Item item) {
		Objects.requireNonNull(material, "material cannot be null");
		Objects.requireNonNull(handler, "handler cannot be null");
		this.overriddenItems.put(material, handler, Optional.ofNullable(item));
	}
}
