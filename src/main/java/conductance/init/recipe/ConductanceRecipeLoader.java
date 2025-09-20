package conductance.init.recipe;

import java.util.Map;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import it.unimi.dsi.fastutil.chars.Char2ObjectArrayMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import conductance.api.CAPI;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.recipe.event.RegisterRecipeEvent;
import conductance.Conductance;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceRecipeLoader {

	public static final char WRENCH = 'W';
	public static final char HAMMER = 'H';
	public static final char WIRE_CUTTERS = 'X';
	private static final Char2ObjectMap<TagKey<Item>> TOOL_LOOKUP = new Char2ObjectArrayMap<>(
			Map.of(ConductanceRecipeLoader.WRENCH, CAPI.TAG_WRENCHES, ConductanceRecipeLoader.HAMMER, CAPI.TAG_HAMMERS, ConductanceRecipeLoader.WIRE_CUTTERS, CAPI.TAG_WIRE_CUTTERS)
	);

	@EventListener(priority = -100)
	private static void addRecipes(final RegisterRecipeEvent event) {
		MaterialRecipes.add(event);
		TierRecipes.add(event);
	}

	private ConductanceRecipeLoader() {
	}
}
