package conductance.core;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import com.mojang.datafixers.util.Either;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import conductance.api.CAPI;
import conductance.api.NCItems;
import conductance.api.NCMaterialGenerationHandlers;
import conductance.api.NCMaterials;

public final class CreativeTabHelper {

	@RequiredArgsConstructor
	public enum Tabs {
		GENERAL(() -> NCItems.WRENCH.value().getDefaultInstance()),
		MACHINE(Items.FURNACE::getDefaultInstance),
		MATERIAL(() -> CAPI.materials().getItem(NCMaterials.ALUMINIUM, NCMaterialGenerationHandlers.INGOT, 1)),
		ORE(() -> CAPI.materials().getItem(NCMaterials.ALUMINIUM, NCMaterialGenerationHandlers.ORE_STONE, 1));

		private final @Getter String name = super.toString().toLowerCase(Locale.ROOT);
		private final @Getter Supplier<ItemStack> icon;
	}

	private static final EnumMap<Tabs, List<Either<ItemLike, ItemStack>>> CONTENT_MAPPING = new EnumMap<>(Tabs.class);

	public static void addToTab(final ItemLike item, final Tabs tab) {
		CreativeTabHelper.CONTENT_MAPPING.computeIfAbsent(tab, k -> new ArrayList<>()).add(Either.left(item));
	}

	public static void addToTab(final ItemStack stack, final Tabs tab) {
		CreativeTabHelper.CONTENT_MAPPING.computeIfAbsent(tab, k -> new ArrayList<>()).add(Either.right(stack));
	}

	public static List<Either<ItemLike, ItemStack>> getTabContent(final Tabs tab) {
		return CreativeTabHelper.CONTENT_MAPPING.getOrDefault(tab, List.of());
	}

	private CreativeTabHelper() {
	}
}
