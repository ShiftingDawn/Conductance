package conductance.compat.ldlib;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import com.lowdragmc.lowdraglib.syncdata.AccessorOp;
import com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor;
import com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.StringPayload;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.machine.recipe.NCRecipeType;

public class RecipeTypeAccessor extends CustomObjectAccessor<NCRecipeType> {

	protected RecipeTypeAccessor() {
		super(NCRecipeType.class, true);
	}

	@Override
	public ITypedPayload<?> serialize(final AccessorOp op, final NCRecipeType value, final HolderLookup.Provider provider) {
		return StringPayload.of(value.getRegistryKey().toString());
	}

	@Override
	@Nullable
	public NCRecipeType deserialize(final AccessorOp op, final ITypedPayload<?> payload, final HolderLookup.Provider provider) {
		if (payload instanceof final StringPayload stringPayload) {
			return CAPI.regs().recipeTypes().get(ResourceLocation.parse(stringPayload.getPayload()));
		}
		return null;
	}
}
