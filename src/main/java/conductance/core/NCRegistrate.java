package conductance.core;

import com.tterrag.registrate.Registrate;
import conductance.Conductance;
import net.minecraft.Util;
import net.neoforged.bus.api.IEventBus;

public final class NCRegistrate extends Registrate {

	NCRegistrate() {
		super(Conductance.MODID);
	}

	public static NCRegistrate create(final IEventBus modEventBus) {
		return Util.make(new NCRegistrate(), ncRegistrate -> ncRegistrate.registerEventListeners(modEventBus));
	}
}