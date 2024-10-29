package conductance.core;

import net.minecraft.Util;
import net.neoforged.bus.api.IEventBus;
import com.tterrag.registrate.Registrate;
import conductance.api.registry.NCRegistrate;
import conductance.Conductance;

public final class ConductanceRegistrate extends Registrate implements NCRegistrate {

	private ConductanceRegistrate() {
		super(Conductance.MODID);
	}

	public static ConductanceRegistrate create(final IEventBus modEventBus) {
		return Util.make(new ConductanceRegistrate(), registrate -> registrate.registerEventListeners(modEventBus));
	}
}
