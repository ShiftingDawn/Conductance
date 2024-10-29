package conductance.core.register;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
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

public class MaterialRegistry implements TaggedSetRegistry<Material, TaggedMaterialSet> {

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
	public Optional<Item> getItem(final TaggedMaterialSet tagType, final Material material) {
		return Optional.ofNullable(this.getItemUnsafe(tagType, material));
	}

	@Override
	public ItemStack getItem(final TaggedMaterialSet tagType, final Material material, final int count) {
		return this.getItem(tagType, material).map(item -> new ItemStack(item, count)).orElseGet(() -> this.getBlock(tagType, material, count));
	}

	@Override
	@Nullable
	public Item getItemUnsafe(final TaggedMaterialSet tagType, final Material material) {
		final List<Item> list = this.itemRegistry.get(tagType, material);
		return list == null || list.isEmpty() ? null : list.get(0);
	}

	@Override
	public Optional<Block> getBlock(final TaggedMaterialSet tagType, final Material material) {
		return Optional.ofNullable(this.getBlockUnsafe(tagType, material));
	}

	@Override
	public ItemStack getBlock(final TaggedMaterialSet tagType, final Material material, final int count) {
		return this.getBlock(tagType, material).map(block -> new ItemStack(block, count)).orElse(ItemStack.EMPTY);
	}

	@Override
	@Nullable
	public Block getBlockUnsafe(final TaggedMaterialSet tagType, final Material material) {
		final List<Block> list = this.blockRegistry.get(tagType, material);
		return list == null || list.isEmpty() ? null : list.get(0);
	}

	// @Override
	// public Optional<Fluid> getFluid(final MaterialFluidGenerator tagType, final
	// Material material) {
	// return Optional.ofNullable(this.getFluidUnsafe(tagType, material));
	// }

	// @Override
	// public FluidStack getFluid(final MaterialFluidGenerator tagType, final
	// Material material, final long amount) {
	// return this.getFluid(tagType, material)
	// .map(fluid -> FluidStack.create(fluid, amount))
	// .orElse(FluidStack.empty());
	// }
	//
	// @Override
	// @Nullable
	// public Fluid getFluidUnsafe(final MaterialFluidGenerator generator, final
	// Material material) {
	// final List<Fluid> list = this.fluidRegistry.get(generator, material);
	// return list == null || list.isEmpty() ? null : list.get(0);
	// }
	//
	// @Override
	// public Optional<BucketItem> getBucket(final MaterialFluidGenerator generator,
	// final Material object) {
	// return Optional.ofNullable(this.getBucketUnsafe(generator, object));
	// }
	//
	// @Override
	// @Nullable
	// public BucketItem getBucketUnsafe(final MaterialFluidGenerator generator,
	// final Material object) {
	// final Fluid fluid = this.getFluidUnsafe(generator, object);
	// return fluid != null ? (BucketItem) fluid.getBucket() : null;
	// }

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

		// TODO handle ores
//		ApiBridge.REGS.REVERSED_ORE_TYPE_MAPPING.clear();
//		CAPI.REGISTRIES.MATERIAL_TAG_TYPES().values().stream()
//				.filter(tt -> tt.getOreType() != null)
//				.forEach(tt -> ApiBridge.REGS.REVERSED_ORE_TYPE_MAPPING.put(tt.getOreType().containingBlockType().get(), tt));
	}

	private static void registerOverriddenComponents() {
		MaterialOverrideRegister.getOverrides().rowMap().forEach((set, mapping) -> mapping.forEach((material, overrides) -> {
			MaterialRegistry.INSTANCE.registerItemInternal(set, material, overrides);
		}));
	}
}
