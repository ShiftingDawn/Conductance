package conductance.core.material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
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
import conductance.api.material.MaterialRegistry;
import conductance.api.material.TaggedMaterialSet;
import conductance.Conductance;

public final class MaterialRegistryImpl implements MaterialRegistry {

	public static final MaterialRegistryImpl INSTANCE = new MaterialRegistryImpl();
	@Getter
	private final Table<TaggedMaterialSet, Material, ItemLike[]> overrideMap = HashBasedTable.create();
	@Getter
	private final Table<TaggedMaterialSet, Material, Long> unitOverrideMap = HashBasedTable.create();
	@Getter
	private final Table<TaggedMaterialSet, Material, ItemEntry<? extends Item>> generatedItemRegistry = HashBasedTable.create();
	@Getter
	private final Table<TaggedMaterialSet, Material, BlockEntry<? extends Block>> generatedBlockRegistry = HashBasedTable.create();
	@Getter
	private final Table<TaggedMaterialSet, Material, FluidEntry<? extends Fluid>> generatedFluidRegistry = HashBasedTable.create();
	private final Table<TaggedMaterialSet, Material, List<Item>> itemRegistry = HashBasedTable.create();
	private final Table<TaggedMaterialSet, Material, List<Block>> blockRegistry = HashBasedTable.create();
	private final Table<TaggedMaterialSet, Material, List<Fluid>> fluidRegistry = HashBasedTable.create();
	private final AtomicBoolean frozen = new AtomicBoolean();

	private MaterialRegistryImpl() {
	}

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
		return list == null || list.isEmpty() ? null : list.getFirst();
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
		return list == null || list.isEmpty() ? null : list.getFirst();
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
		return list == null || list.isEmpty() ? null : list.getFirst();
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

	public void register(final TaggedMaterialSet tagType, final Material material, final ItemEntry<? extends Item> item) {
		if (this.frozen.get()) {
			throw new IllegalStateException("Trying to register item in frozen MaterialRegistry!");
		}
		this.generatedItemRegistry.put(tagType, material, item);
	}

	public void register(final TaggedMaterialSet taggedSet, final Material material, final BlockEntry<? extends Block> block) {
		if (this.frozen.get()) {
			throw new IllegalStateException("Trying to register block in frozen MaterialRegistry!");
		}
		this.generatedBlockRegistry.put(taggedSet, material, block);
	}

	public void register(final TaggedMaterialSet taggedSet, final Material material, final FluidEntry<? extends Fluid> fluid) {
		if (this.frozen.get()) {
			throw new IllegalStateException("Trying to register fluid in frozen MaterialRegistry!");
		}
		this.generatedFluidRegistry.put(taggedSet, material, fluid);
	}

	public void addOverride(final TaggedMaterialSet set, final Material material, final ItemLike[] overrides) {
		if (this.frozen.get()) {
			throw new IllegalStateException("Trying to register override in frozen MaterialRegistry!");
		}
		this.overrideMap.put(set, material, overrides);
	}

	public void addUnitOverride(final TaggedMaterialSet set, final Material material, final long newValue) {
		if (this.frozen.get()) {
			throw new IllegalStateException("Trying to register unit override in frozen MaterialRegistry!");
		}
		this.unitOverrideMap.put(set, material, newValue);
	}

	@Override
	public boolean hasOverride(final TaggedMaterialSet set, final Material material) {
		return this.overrideMap.contains(set, material);
	}

	@Override
	public boolean hasUnitOverride(final TaggedMaterialSet set, final Material material) {
		return this.unitOverrideMap.contains(set, material);
	}

	@Override
	public long getUnitOverride(final TaggedMaterialSet set, final Material material) {
		final Long result = this.unitOverrideMap.get(set, material);
		return Objects.requireNonNullElse(result, -1L);
	}

	public void freeze() {
		if (this.frozen.getAndSet(true)) {
			throw new IllegalStateException("Trying to freeze already frozen MaterialRegistry!");
		}
		Conductance.LOGGER.info("MaterialRegistry has been frozen!");
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

		this.registerOverriddenComponents();

		this.generatedItemRegistry.cellSet().forEach(cell -> this.registerItemInternal(cell.getRowKey(), cell.getColumnKey(), cell.getValue().get()));
		this.generatedBlockRegistry.cellSet().forEach(cell -> this.registerBlockInternal(cell.getRowKey(), cell.getColumnKey(), cell.getValue().get()));
		this.generatedFluidRegistry.cellSet().forEach(cell -> this.registerFluidInternal(cell.getRowKey(), cell.getColumnKey(), cell.getValue().get()));
	}

	private void registerOverriddenComponents() {
		this.overrideMap.rowMap().forEach((set, mapping) -> mapping.forEach((material, overrides) -> {
			Arrays.stream(overrides).forEach(override -> {
				if (set.hasBlocks() && override instanceof final Block block) {
					MaterialRegistryImpl.INSTANCE.registerBlockInternal(set, material, block);
				}
				if (set.hasItems()) {
					MaterialRegistryImpl.INSTANCE.registerItemInternal(set, material, override);
				}
			});
		}));
	}
}
