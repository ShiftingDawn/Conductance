package conductance.init;

import conductance.api.machine.recipe.IRecipe;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.plugin.SyncFieldSerializerRegister;
import conductance.api.util.tier.Tier;
import conductance.sync.NBTSerializableHandler;
import conductance.sync.RecipeSerializer;
import conductance.sync.RecipeTypeSerializer;
import conductance.sync.TagSerializer;
import conductance.sync.TierSerializer;

public final class ConductanceSyncFieldSerializers {

	public static void init(final SyncFieldSerializerRegister register) {
		register.register(TagSerializer.class, TagSerializer::new, new NBTSerializableHandler());

		register.register(TierSerializer.class, TierSerializer::new, Tier.class, false);
		register.register(RecipeTypeSerializer.class, RecipeTypeSerializer::new, NCRecipeType.class, false);
		register.register(RecipeSerializer.class, RecipeSerializer::new, IRecipe.class, false);
	}

	private ConductanceSyncFieldSerializers() {
	}
}
