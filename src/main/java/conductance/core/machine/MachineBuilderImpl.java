package conductance.core.machine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import com.lowdragmc.lowdraglib.client.renderer.IRenderer;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.BlockEntityBuilder;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.machine.IMachineBlockItem;
import conductance.api.machine.MachineBlock;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineBlockEntityFactory;
import conductance.api.machine.MachineBlockFactory;
import conductance.api.machine.MachineBlockItem;
import conductance.api.machine.MachineBlockItemFactory;
import conductance.api.machine.MachineBuilder;
import conductance.api.machine.MachineType;
import conductance.api.machine.gui.MachineGuiSupplier;
import conductance.api.machine.recipe.IRecipe;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.machine.render.MachineOverlayRenderer;
import conductance.api.machine.render.WorkableMachineRenderer;
import conductance.api.util.RotationState;
import conductance.Conductance;
import conductance.core.recipe.RecipeTypeImpl;
import conductance.runtimepack.client.MachineBlockModelHandler;
import static conductance.core.apiimpl.ApiBridge.getRegistrate;

public class MachineBuilderImpl<T extends MachineBlockEntity<T>> implements MachineBuilder<T> {

	private final String registryKey;
	private MachineBlockFactory<T> blockFactory = MachineBlock::new;
	private MachineBlockItemFactory<T> itemFactory = MachineBlockItem::new;
	private MachineBlockEntityFactory<T> blockEntityFactory;
	@Getter
	private NCRecipeType[] recipeTypes = new NCRecipeType[0];
	private Object2IntMap<IRecipeElementType<?>> recipeOutputLimits = new Object2IntOpenHashMap<>();
	private BiFunction<MachineBlockEntity<?>, IRecipe, IRecipe> recipeModifier = (machine, recipe) -> recipe;
	private RotationState rotationState = RotationState.HORIZONTAL;
	private IRenderer modelRenderer;
	@Getter
	private MachineGuiSupplier guiSupplier;
	private final List<Component> tooltips = new ArrayList<>();
	@Nullable
	private BiConsumer<ItemStack, List<Component>> tooltipBuilder;
	@Nullable
	private String localized = null;

	public MachineBuilderImpl(final String registryKey, final MachineBlockEntityFactory<T> machineBlockEntityFactory) {
		this.registryKey = registryKey;
		this.blockEntityFactory = machineBlockEntityFactory;
		this.defaultModelRenderer(Conductance.id("block/machine_casing_tiered"));
	}

	@Override
	public MachineBuilder<T> blockFactory(final MachineBlockFactory<T> factory) {
		this.blockFactory = factory;
		return this;
	}

	@Override
	public MachineBuilder<T> itemFactory(final MachineBlockItemFactory<T> factory) {
		this.itemFactory = factory;
		return this;
	}

	@Override
	public MachineBuilder<T> blockEntityFactory(final MachineBlockEntityFactory<T> factory) {
		this.blockEntityFactory = factory;
		return this;
	}

	@SuppressWarnings("removal")
	private BlockEntry<? extends MachineBlock<T>> createBlock(final MachineTypeImpl<T> machineType) {
		final BlockBuilder<MachineBlock<T>, Registrate> blockBuilder = getRegistrate().block(this.registryKey, props -> {
			RotationState.set(this.rotationState);
			final MachineBlock<T> block = this.blockFactory.newInstance(props, machineType);
			RotationState.clear();
			return block;
		});
		blockBuilder
				.initialProperties(() -> Blocks.IRON_BLOCK)
				.blockstate(NonNullBiConsumer.noop())
				.addLayer(() -> RenderType::cutoutMipped)
				.item((block, props) -> {
					final IMachineBlockItem<T> item = this.itemFactory.newInstance(block, props);
					if (!(item instanceof Item)) {
						throw new IllegalArgumentException("Machine block item %s is not an Item".formatted(item.getClass().getName()));
					}
					return (Item) item;
				})
				.model(NonNullBiConsumer.noop())
				.build();
		return blockBuilder.register();
	}

	private BlockEntityEntry<T> createBlockEntity(final MachineTypeImpl<T> machineType) {
		final BlockEntityBuilder<T, Registrate> builder = getRegistrate().blockEntity(this.registryKey, (type, pos, state) -> this.blockEntityFactory.newInstance(machineType, pos, state));
		builder.validBlock(machineType.getBlock());
		return builder.register();
	}

	@Override
	public MachineBuilder<T> recipeType(final NCRecipeType recipeType, final NCRecipeType... moreTypes) {
		this.recipeTypes = new NCRecipeType[moreTypes.length + 1];
		this.recipeTypes[0] = recipeType;
		System.arraycopy(moreTypes, 0, this.recipeTypes, 1, moreTypes.length);
		return this;
	}

	@Override
	public MachineBuilder<T> recipeOutputLimits(final Object2IntMap<IRecipeElementType<?>> outputLimits) {
		this.recipeOutputLimits = outputLimits;
		return this;
	}

	@Override
	public MachineBuilder<T> recipeModifier(final BiFunction<MachineBlockEntity<?>, IRecipe, IRecipe> modifier) {
		this.recipeModifier = modifier;
		return this;
	}

	@Override
	public MachineBuilder<T> rotationState(final RotationState rotState) {
		this.rotationState = rotState;
		return this;
	}

	@Override
	public MachineBuilder<T> modelRenderer(final IRenderer renderer) {
		this.modelRenderer = renderer;
		return this;
	}

	@Override
	public MachineBuilder<T> defaultModelRenderer(final ResourceLocation baseModelLocation, @Nullable final ResourceLocation overlayModelLocation) {
		final ResourceLocation overlayLocation;
		if (overlayModelLocation != null) {
			overlayLocation = overlayModelLocation;
			MachineBlockModelHandler.remove(this.registryKey);
		} else {
			overlayLocation = Conductance.id("block/machine/%s".formatted(this.registryKey));
			MachineBlockModelHandler.add(this.registryKey, overlayLocation);
		}
		return this.modelRenderer(new MachineOverlayRenderer(baseModelLocation, overlayLocation));
	}

	@Override
	public MachineBuilder<T> tieredModelRenderer(final ResourceLocation baseModelLocation, final String baseMachineKey, @Nullable final ResourceLocation overlayModelLocation) {
		final ResourceLocation overlayLocation;
		if (overlayModelLocation != null) {
			overlayLocation = overlayModelLocation;
			MachineBlockModelHandler.remove(this.registryKey);
		} else {
			overlayLocation = Conductance.id("block/machine/%s".formatted(baseMachineKey));
			MachineBlockModelHandler.add(this.registryKey, overlayLocation);
		}
		return this.modelRenderer(new MachineOverlayRenderer(baseModelLocation, overlayLocation));
	}

	@Override
	public MachineBuilder<T> workableModelRenderer(final ResourceLocation baseModelLocation) {
		MachineBlockModelHandler.remove(this.registryKey);
		return this.modelRenderer(new WorkableMachineRenderer(baseModelLocation, Conductance.id("block/machine/%s".formatted(this.registryKey))));
	}

	@Override
	public MachineBuilder<T> tieredWorkableModelRenderer(final ResourceLocation baseModelLocation, final String baseMachineKey) {
		MachineBlockModelHandler.remove(this.registryKey);
		return this.modelRenderer(new WorkableMachineRenderer(baseModelLocation, Conductance.id("block/machine/%s".formatted(baseMachineKey))));
	}

	@Override
	public MachineBuilder<T> guiSupplier(final MachineGuiSupplier supplier) {
		this.guiSupplier = supplier;
		return this;
	}

	@Override
	public MachineBuilder<T> tooltip(final Component... lines) {
		Arrays.stream(lines).filter(Objects::nonNull).forEach(this.tooltips::add);
		return this;
	}

	@Override
	public MachineBuilder<T> tooltip(final BiConsumer<ItemStack, List<Component>> builder) {
		this.tooltipBuilder = builder;
		return this;
	}

	@Override
	public MachineBuilder<T> localized(final String localizedName) {
		this.localized = localizedName;
		return this;
	}

	@Override
	public MachineType<T> build() {
		final MachineTypeImpl<T> machineType = Util.make(new MachineTypeImpl<>(this.registryKey), result -> {
			result.setBlock(this.createBlock(result));
			result.setBlockEntityType(this.createBlockEntity(result));
			result.setRecipeTypes(this.recipeTypes);
			result.setRecipeOutputLimits(this.recipeOutputLimits);
			result.setRecipeModifier(this.recipeModifier);
			result.setModelRenderer(this.modelRenderer);
			result.setGuiSupplier(this.guiSupplier);
			result.setLocalizedName(this.localized);
			result.setTooltipBuilder((stack, tooltip) -> {
				tooltip.addAll(this.tooltips);
				if (this.tooltipBuilder != null) {
					this.tooltipBuilder.accept(stack, tooltip);
				}
			});
		});
		machineType.validate();
		CAPI.regs().machines().register(machineType.getRegistryKey(), machineType);
		Arrays.stream(this.recipeTypes).forEach(type -> ((RecipeTypeImpl) type).setRecipeTypeIcon(() -> new ItemStack(machineType.getBlock().get())));
		return machineType;
	}
}
