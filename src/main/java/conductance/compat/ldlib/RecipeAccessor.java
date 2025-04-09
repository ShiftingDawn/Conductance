package conductance.compat.ldlib;

import net.minecraft.core.HolderLookup;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.neoforge.network.connection.ConnectionType;
import com.lowdragmc.lowdraglib.syncdata.AccessorOp;
import com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor;
import com.lowdragmc.lowdraglib.syncdata.payload.FriendlyBufPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.machine.recipe.IRecipe;
import conductance.core.recipe.RecipeSerializerImpl;

public class RecipeAccessor extends CustomObjectAccessor<IRecipe> {

	public RecipeAccessor() {
		super(IRecipe.class, true);
	}

	@Override
	public ITypedPayload<?> serialize(final AccessorOp op, final IRecipe value, final HolderLookup.Provider provider) {
		final RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(new FriendlyByteBuf(Unpooled.buffer()), CAPI.frozenRegistry(), ConnectionType.NEOFORGE);
		RecipeSerializerImpl.toNetwork(buf, value);
		return FriendlyBufPayload.of(buf);
	}

	@Override
	@Nullable
	public IRecipe deserialize(final AccessorOp op, final ITypedPayload<?> payload, final HolderLookup.Provider provider) {
		if (payload instanceof final FriendlyBufPayload friendlyBufPayload) {
			final RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(friendlyBufPayload.getPayload(), CAPI.frozenRegistry(), ConnectionType.NEOFORGE);
			return RecipeSerializerImpl.fromNetwork(buf);
		}
		return null;
	}
}
