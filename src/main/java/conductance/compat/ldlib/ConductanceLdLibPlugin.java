package conductance.compat.ldlib;

import com.lowdragmc.lowdraglib.plugin.ILDLibPlugin;
import com.lowdragmc.lowdraglib.plugin.LDLibPlugin;
import com.lowdragmc.lowdraglib.syncdata.TypedPayloadRegistries;
import com.lowdragmc.lowdraglib.syncdata.payload.FriendlyBufPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.StringPayload;

@LDLibPlugin
public class ConductanceLdLibPlugin implements ILDLibPlugin {

	@Override
	public void onLoad() {
		TypedPayloadRegistries.register(StringPayload.class, StringPayload::new, new TierAccessor(), 1000);
		TypedPayloadRegistries.register(StringPayload.class, StringPayload::new, new RecipeTypeAccessor(), 1000);
		TypedPayloadRegistries.register(FriendlyBufPayload.class, FriendlyBufPayload::new, new RecipeAccessor(), 1000);
	}
}
