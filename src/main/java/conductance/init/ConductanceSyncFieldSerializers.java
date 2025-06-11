package conductance.init;

import net.minecraft.world.item.ItemStack;
import conductance.api.machine.recipe.IRecipe;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.plugin.RegisterFieldSerializerEvent;
import conductance.api.tier.Tier;
import conductance.Conductance;
import conductance.init.sync.ItemStackSerializer;
import conductance.init.sync.NBTSerializableHandler;
import conductance.init.sync.RecipeSerializer;
import conductance.init.sync.RecipeTypeSerializer;
import conductance.init.sync.TagSerializer;
import conductance.init.sync.TierSerializer;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceSyncFieldSerializers {

	@EventListener(priority = -100)
	private static void init(final RegisterFieldSerializerEvent event) {
		event.register(TagSerializer.class, TagSerializer::new, new NBTSerializableHandler());
		event.register(ItemStackSerializer.class, ItemStackSerializer::new, ItemStack.class, true);

		event.register(TierSerializer.class, TierSerializer::new, Tier.class, false);
		event.register(RecipeTypeSerializer.class, RecipeTypeSerializer::new, NCRecipeType.class, false);
		event.register(RecipeSerializer.class, RecipeSerializer::new, IRecipe.class, false);
	}

	private ConductanceSyncFieldSerializers() {
	}
}
