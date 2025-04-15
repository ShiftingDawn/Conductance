package conductance.init;

import conductance.api.plugin.TierRegister;
import conductance.core.apiimpl.TierRegistryImpl;
import static conductance.api.NCTiers.LV;
import static net.minecraft.ChatFormatting.DARK_BLUE;

public final class ConductanceTiers {

	public static void init(final TierRegister register) {
		LV = register.register("lv", DARK_BLUE + "LV", 0x004fff).previous(TierRegistryImpl.EMPTY).build();
	}

	private ConductanceTiers() {
	}
}
