package conductance.core.register;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.FluidEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.registry.TaggedSetRegistry;
import conductance.Conductance;

public final class MaterialRegistry implements TaggedSetRegistry<Material, TaggedMaterialSet> {

	public static final MaterialRegistry INSTANCE = new MaterialRegistry();
	@Getter
	private final Table<TaggedMaterialSet, Material, ItemEntry<? extends Item>> generatedItemRegistry = HashBasedTable.create();
	@Getter
	private final Table<TaggedMaterialSet, Material, BlockEntry<? extends Block>> generatedBlockRegistry = HashBasedTable.create();
	@Getter
	private final Table<TaggedMaterialSet, Material, FluidEntry<? extends Fluid>> generatedFluidRegistry = HashBasedTable.create();
	private final Table<TaggedMaterialSet, Material, List<Item>> itemRegistry = HashBasedTable.create();
	private final Table<TaggedMaterialSet, Material, List<Block>> blockRegistry = HashBasedTable.create();
	private final Table<TaggedMaterialSet, Material, List<Fluid>> fluidRegistry = HashBasedTable.create();
	private boolean frozen = false;

	@Override
	public Optional<Item> getItem(final TaggedMaterialSet taggedSet, final Material material) {
		return Optional.ofNullable(this.getItemUnsafe(taggedSet, material));
	}

	@Override
	public ItemStack getItem(final TaggedMaterialSet taggedSet, final Material material, final int count) {
		return this.getItem(taggedSet, material).map(item -> new ItemStack(item, count)).orElseGet(() -> this.getBlock(taggedSet, material, count));
	}

	@Override
	@Nullable
	public Item getItemUnsafe(final TaggedMaterialSet taggedSet, final Material material) {
		final List<Item> list = this.itemRegistry.get(taggedSet, material);
		return list == null || list.isEmpty() ? null : list.get(0);
	}

	@Override
	public Optional<Block> getBlock(final TaggedMaterialSet taggedSet, final Material material) {
		return Optional.ofNullable(this.getBlockUnsafe(taggedSet, material));
	}

	@Override
	public ItemStack getBlock(final TaggedMaterialSet taggedSet, final Material material, final int count) {
		return this.getBlock(taggedSet, material).map(block -> new ItemStack(block, count)).orElse(ItemStack.EMPTY);
	}

	@Override
	@Nullable
	public Block getBlockUnsafe(final TaggedMaterialSet taggedSet, final Material material) {
		final List<Block> list = this.blockRegistry.get(taggedSet, material);
		return list == null || list.isEmpty() ? null : list.get(0);
	}

	@Override
	public Optional<Fluid> getFluid(final TaggedMaterialSet taggedSet, final Material material) {
		return Optional.ofNullable(this.getFluidUnsafe(taggedSet, material));
	}

	@Override
	public FluidStack getFluid(final TaggedMaterialSet taggedSet, final Material material, final int amount) {
		return this.getFluid(taggedSet, material).map(fluid -> new FluidStack(fluid, amount)).orElse(FluidStack.EMPTY);
	}

	@Override
	@Nullable
	public Fluid getFluidUnsafe(final TaggedMaterialSet taggedSet, final Material material) {
		final List<Fluid> list = this.fluidRegistry.get(taggedSet, material);
		return list == null || list.isEmpty() ? null : list.get(0);
	}

	@Override
	public Optional<BucketItem> getBucket(final TaggedMaterialSet taggedSet, final Material object) {
		return Optional.ofNullable(this.getBucketUnsafe(taggedSet, object));
	}

	@Override
	@Nullable
	public BucketItem getBucketUnsafe(final TaggedMaterialSet taggedSet, final Material object) {
		final Fluid fluid = this.getFluidUnsafe(taggedSet, object);
		return fluid != null ? (BucketItem) fluid.getBucket() : null;
	}

	@Override
	public void register(final TaggedMaterialSet tagType, final Material material, final ItemEntry<? extends Item> item) {
		if (this.frozen) {
			throw new IllegalStateException("Trying to register item in frozen MaterialRegistry!");
		}
		this.generatedItemRegistry.put(tagType, material, item);
	}

	@Override
	public void register(final TaggedMaterialSet taggedSet, final Material material, final BlockEntry<? extends Block> block) {
		if (this.frozen) {
			throw new IllegalStateException("Trying to register block in frozen MaterialRegistry!");
		}
		this.generatedBlockRegistry.put(taggedSet, material, block);
	}

	@Override
	public void register(final TaggedMaterialSet taggedSet, final Material material, final FluidEntry<? extends Fluid> fluid) {
		if (this.frozen) {
			throw new IllegalStateException("Trying to register fluid in frozen MaterialRegistry!");
		}
		this.generatedFluidRegistry.put(taggedSet, material, fluid);
	}

	public void freeze() {
		Conductance.LOGGER.info("MaterialRegistry has been frozen!");
		this.frozen = true;
	}

	public Table<TaggedMaterialSet, Material, List<Item>> getItemTable() {
		return ImmutableTable.copyOf(this.itemRegistry);
	}

	public Table<TaggedMaterialSet, Material, List<Block>> getBlockTable() {
		return ImmutableTable.copyOf(this.blockRegistry);
	}

	public Table<TaggedMaterialSet, Material, List<Fluid>> getFluidTable() {
		return ImmutableTable.copyOf(this.fluidRegistry);
	}

	private void registerItemInternal(final TaggedMaterialSet taggedSet, final Material material, final ItemLike... items) {
		List<Item> list = this.itemRegistry.get(taggedSet, material);
		if (list == null) {
			list = new ArrayList<>();
			this.itemRegistry.put(taggedSet, material, list);
		}
		for (final ItemLike item : items) {
			list.add(item.asItem());
		}
	}

	private void registerBlockInternal(final TaggedMaterialSet taggedSet, final Material material, final Block... blocks) {
		List<Block> list = this.blockRegistry.get(taggedSet, material);
		if (list == null) {
			list = new ArrayList<>();
			this.blockRegistry.put(taggedSet, material, list);
		}
		list.addAll(Arrays.asList(blocks));
	}

	private void registerFluidInternal(final TaggedMaterialSet taggedSet, final Material material, final Fluid... fluids) {
		List<Fluid> list = this.fluidRegistry.get(taggedSet, material);
		if (list == null) {
			list = new ArrayList<>();
			this.fluidRegistry.put(taggedSet, material, list);
		}
		list.addAll(Arrays.asList(fluids));
	}

	public void reload() {
		this.itemRegistry.clear();
		this.blockRegistry.clear();
		this.fluidRegistry.clear();

		MaterialRegistry.registerOverriddenComponents();

		this.generatedItemRegistry.cellSet().forEach(cell -> this.registerItemInternal(cell.getRowKey(), cell.getColumnKey(), cell.getValue().get()));
		this.generatedBlockRegistry.cellSet().forEach(cell -> this.registerBlockInternal(cell.getRowKey(), cell.getColumnKey(), cell.getValue().get()));
		this.generatedFluidRegistry.cellSet().forEach(cell -> this.registerFluidInternal(cell.getRowKey(), cell.getColumnKey(), cell.getValue().get()));
	}

	private static void registerOverriddenComponents() {
		MaterialOverrideRegister.getOverrides().rowMap().forEach((set, mapping) -> mapping.forEach((material, overrides) -> {
			Arrays.stream(overrides).forEach(override -> {
				if (set.isBlockGenerator() && override instanceof final Block block) {
					MaterialRegistry.INSTANCE.registerBlockInternal(set, material, block);
				}
				if (set.isItemGenerator()) {
					MaterialRegistry.INSTANCE.registerItemInternal(set, material, override);
				}
			});
		}));
	}
}
