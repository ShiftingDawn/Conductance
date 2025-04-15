package conductance.compat.ldlib;

import net.minecraft.core.HolderLookup;
import com.lowdragmc.lowdraglib.syncdata.AccessorOp;
import com.lowdragmc.lowdraglib.syncdata.accessor.CustomObjectAccessor;
import com.lowdragmc.lowdraglib.syncdata.payload.ITypedPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.StringPayload;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.util.tier.Tier;

public class TierAccessor extends CustomObjectAccessor<Tier> {

	protected TierAccessor() {
		super(Tier.class, true);
	}

	@Override
	public ITypedPayload<?> serialize(final AccessorOp op, final Tier value, final HolderLookup.Provider provider) {
		return StringPayload.of(value.getRegistryKey());
	}

	@Override
	@Nullable
	public Tier deserialize(final AccessorOp op, final ITypedPayload<?> payload, final HolderLookup.Provider provider) {
		if (payload instanceof final StringPayload stringPayload) {
			return CAPI.regs().tiers().get(stringPayload.getPayload());
		}
		return null;
	}
}
