package conductance.core.register;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.builders.NoConfigBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.CreativeModeTabModifier;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import conductance.Conductance;
import conductance.init.ConductanceCreativeTabs;
import conductance.init.item.IConductanceItem;

public final class ConductanceRegistrate extends Registrate {

	private final Map<RegistryEntry<CreativeModeTab, CreativeModeTab>, List<RegistryEntry<?, ?>>> creativeTabLookup = Collections.synchronizedMap(new IdentityHashMap<>());

	private ConductanceRegistrate() {
		super(Conductance.MODID);
	}

	static ConductanceRegistrate create() {
		return Util.make(new ConductanceRegistrate(), registrate -> {
			registrate.addRegisterCallback(Registries.BLOCK, () -> registrate.getAll(Registries.BLOCK).forEach(entry -> {
				if (entry.get() instanceof final IConductanceItem conductanceItem) {
					registrate.setCreativeTab(entry, conductanceItem.getCreativeTab());
				}
			}));
			registrate.addRegisterCallback(Registries.ITEM, () -> registrate.getAll(Registries.ITEM).forEach(entry -> {
				if (entry.get() instanceof final BlockItem blockItem && blockItem.getBlock() instanceof IConductanceItem) {
					return;
				}
				RegistryEntry<CreativeModeTab, CreativeModeTab> tab = ConductanceCreativeTabs.GENERAL;
				if (entry.get() instanceof final IConductanceItem conductanceItem) {
					tab = conductanceItem.getCreativeTab();
				}
				registrate.setCreativeTab(entry, tab);
			}));
		});
	}

	public void setCreativeTab(final RegistryEntry<?, ?> entry, final RegistryEntry<CreativeModeTab, CreativeModeTab> tab) {
		this.creativeTabLookup.computeIfAbsent(tab, k -> Collections.synchronizedList(new ArrayList<>())).add(entry);
	}

	public List<RegistryEntry<?, ?>> getContentsForTab(final RegistryEntry<CreativeModeTab, CreativeModeTab> tab) {
		return this.creativeTabLookup.getOrDefault(tab, List.of());
	}

	@Override
	public Registrate modifyCreativeModeTab(final ResourceKey<CreativeModeTab> creativeModeTab, final Consumer<CreativeModeTabModifier> modifier) {
		return this;
	}

	@Override
	public <T extends Item, P> ItemBuilder<T, P> item(final P parent, final String name, final NonNullFunction<Item.Properties, T> factory) {
		return super.item(parent, name, factory)
				.setData(ProviderType.LANG, NonNullBiConsumer.noop());
	}

	@Override
	public <T extends Block, P> BlockBuilder<T, P> block(final P parent, final String name, final NonNullFunction<BlockBehaviour.Properties, T> factory) {
		return super.block(parent, name, factory)
				.setData(ProviderType.BLOCKSTATE, NonNullBiConsumer.noop())
				.setData(ProviderType.LANG, NonNullBiConsumer.noop())
				.setData(ProviderType.LOOT, NonNullBiConsumer.noop());
	}

	@Override
	public <P> FluidBuilder<BaseFlowingFluid.Flowing, P> fluid(final P parent, final String name, final ResourceLocation stillTexture, final ResourceLocation flowingTexture) {
		return super.fluid(parent, name, stillTexture, flowingTexture)
				.setData(ProviderType.LANG, NonNullBiConsumer.noop());
	}

	@Override
	public <P> FluidBuilder<BaseFlowingFluid.Flowing, P> fluid(
			final P parent, final String name, final ResourceLocation stillTexture, final ResourceLocation flowingTexture,
			final FluidBuilder.FluidTypeFactory typeFactory) {
		return super.fluid(parent, name, stillTexture, flowingTexture, typeFactory)
				.setData(ProviderType.LANG, NonNullBiConsumer.noop());
	}

	@Override
	public <P> FluidBuilder<BaseFlowingFluid.Flowing, P> fluid(final P parent, final String name, final ResourceLocation stillTexture, final ResourceLocation flowingTexture, final NonNullSupplier<FluidType> fluidType) {
		return super.fluid(parent, name, stillTexture, flowingTexture, fluidType)
				.setData(ProviderType.LANG, NonNullBiConsumer.noop());
	}

	@Override
	public <T extends BaseFlowingFluid, P> FluidBuilder<T, P> fluid(
			final P parent, final String name, final ResourceLocation stillTexture, final ResourceLocation flowingTexture,
			final NonNullFunction<BaseFlowingFluid.Properties, T> fluidFactory) {
		return super.fluid(parent, name, stillTexture, flowingTexture, fluidFactory)
				.setData(ProviderType.LANG, NonNullBiConsumer.noop());
	}

	@Override
	public <T extends BaseFlowingFluid, P> FluidBuilder<T, P> fluid(
			final P parent, final String name, final ResourceLocation stillTexture, final ResourceLocation flowingTexture, final FluidBuilder.FluidTypeFactory typeFactory,
			final NonNullFunction<BaseFlowingFluid.Properties, T> fluidFactory) {
		return super.fluid(parent, name, stillTexture, flowingTexture, typeFactory, fluidFactory)
				.setData(ProviderType.LANG, NonNullBiConsumer.noop());
	}

	@Override
	public <T extends BaseFlowingFluid, P> FluidBuilder<T, P> fluid(
			final P parent, final String name, final ResourceLocation stillTexture, final ResourceLocation flowingTexture, final NonNullSupplier<FluidType> fluidType,
			final NonNullFunction<BaseFlowingFluid.Properties, T> fluidFactory) {
		return super.fluid(parent, name, stillTexture, flowingTexture, fluidType, fluidFactory)
				.setData(ProviderType.LANG, NonNullBiConsumer.noop());
	}

	@Override
	public <P> NoConfigBuilder<CreativeModeTab, CreativeModeTab, P> defaultCreativeTab(final P parent, final String name, final Consumer<CreativeModeTab.Builder> config) {
		return this.generic(parent, name, Registries.CREATIVE_MODE_TAB, () -> {
			final var builder = CreativeModeTab.builder().icon(() -> this.getAll(Registries.ITEM).stream().findFirst().map(ItemEntry::cast).map(ItemEntry::asStack).orElse(new ItemStack(Items.AIR)));
			config.accept(builder);
			return builder.build();
		});
	}
}
