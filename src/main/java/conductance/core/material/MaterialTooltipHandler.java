package conductance.core.material;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;

final class MaterialTooltipHandler {

	private record Entry(MaterialGenerationHandler handler, Material material) {
	}

	private static final Map<TagKey<Item>, Entry> ITEM_TAG_CACHE = new ConcurrentHashMap<>();
	private static final Map<TagKey<Fluid>, Entry> FLUID_TAG_CACHE = new ConcurrentHashMap<>();
	private static final Map<Item, Entry> CACHE = new ConcurrentHashMap<>();

	public static void initialize() {
		NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, ItemTooltipEvent.class, MaterialTooltipHandler::onItemTooltip);
	}

	public static void onItemTooltip(final ItemTooltipEvent event) {
		if (event.getItemStack().isEmpty()) {
			return;
		}
		final List<Component> list = event.getToolTip();
		final Item item = event.getItemStack().getItem();
		final Entry entry;
		if (item instanceof final BucketItem bucketItem) {
			entry = MaterialTooltipHandler.findBucketEntry(bucketItem);
		} else {
			entry = MaterialTooltipHandler.CACHE.computeIfAbsent(item, MaterialTooltipHandler::findEntryForItem);
		}
		if (entry != null) {
			final Component component = Component.literal(entry.material().getChemicalFormula()).withStyle(ChatFormatting.AQUA);
			if (list.size() == 1) {
				list.addLast(component);
			} else {
				list.add(1, component);
			}
		}
		if (item instanceof final BucketItem bucketItem) {
			final FluidType type = bucketItem.content.getFluidType();
			list.add(Component.translatable("tooltip.material_bucket.temperature", type.getTemperature()));
			list.add(Component.translatable("tooltip.material_bucket.density", type.getDensity()));
			list.add(Component.translatable("tooltip.material_bucket.viscosity", type.getViscosity()));
		}
	}

	@SuppressWarnings("deprecation")
	private static @Nullable Entry findEntryForItem(final Item item) {
		return item.builtInRegistryHolder().tags()
			.map(MaterialTooltipHandler::getEntryForItemTag)
			.filter(Objects::nonNull)
			.findFirst()
			.orElse(null);
	}

	private static @Nullable Entry findBucketEntry(final BucketItem bucket) {
		return MaterialTooltipHandler.findEntryForFluid(bucket.content);
	}

	@SuppressWarnings("deprecation")
	private static @Nullable Entry findEntryForFluid(final Fluid fluid) {
		return fluid.builtInRegistryHolder().tags()
			.map(MaterialTooltipHandler::getEntryForFluidTag)
			.filter(Objects::nonNull)
			.findFirst()
			.orElse(null);
	}

	private static @Nullable Entry getEntryForItemTag(final TagKey<Item> tag) {
		if (MaterialTooltipHandler.ITEM_TAG_CACHE.isEmpty()) {
			final Set<TagKey<Item>> tags = BuiltInRegistries.ITEM.getTags().map(HolderSet.Named::key).collect(Collectors.toSet());
			for (final MaterialGenerationHandler handler : CAPI.regs().materialGenerationHandlers()) {
				for (final Material material : CAPI.regs().materials()) {
					for (final TagKey<Item> entryTag : handler.getEntryTags(BuiltInRegistries.ITEM, material)) {
						if (tags.contains(entryTag)) {
							MaterialTooltipHandler.ITEM_TAG_CACHE.put(entryTag, new Entry(handler, material));
							tags.remove(entryTag);
						}
					}
				}
			}
		}
		return MaterialTooltipHandler.ITEM_TAG_CACHE.get(tag);
	}

	private static @Nullable Entry getEntryForFluidTag(final TagKey<Fluid> tag) {
		if (MaterialTooltipHandler.FLUID_TAG_CACHE.isEmpty()) {
			final Set<TagKey<Fluid>> tags = BuiltInRegistries.FLUID.getTags().map(HolderSet.Named::key).collect(Collectors.toSet());
			for (final MaterialGenerationHandler handler : CAPI.regs().materialGenerationHandlers()) {
				for (final Material material : CAPI.regs().materials()) {
					for (final TagKey<Fluid> entryTag : handler.getEntryTags(BuiltInRegistries.FLUID, material)) {
						if (tags.contains(entryTag)) {
							MaterialTooltipHandler.FLUID_TAG_CACHE.put(entryTag, new Entry(handler, material));
							tags.remove(entryTag);
						}
					}
				}
			}
		}
		return MaterialTooltipHandler.FLUID_TAG_CACHE.get(tag);
	}

	private MaterialTooltipHandler() {
	}
}
